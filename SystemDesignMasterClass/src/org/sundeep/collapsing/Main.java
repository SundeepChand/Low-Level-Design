package org.sundeep.collapsing;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class MetricsUtil {
    static private final AtomicInteger numServiceCalls = new AtomicInteger(0);
    static private final AtomicInteger numCacheHits = new AtomicInteger(0);
    static private final AtomicInteger numCacheMisses = new AtomicInteger(0);

    static public void recordServiceCall() {
        numServiceCalls.incrementAndGet();
    }

    static public void recordCacheHit() {
        numCacheHits.incrementAndGet();
    }

    static public void recordCacheMiss() {
        numCacheMisses.incrementAndGet();
    }

    static public void printMetrics() {
        System.out.println("[METRICS] Service Calls: " + numServiceCalls.get() +
                           ", Cache Hits: " + numCacheHits.get() +
                           ", Cache Misses: " + numCacheMisses.get());
    }
}

class Blog {
    private String id;
    private String title;

    public Blog(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}

class InMemoryCache<K, V> {
    private final ConcurrentHashMap<K, V> mp = new ConcurrentHashMap<>();

    public V get(K key) {
        V result = mp.get(key);
        if (result == null) {
            System.out.println("[CACHE][WARN]: Cache miss for key " + key);
            MetricsUtil.recordCacheMiss();
        } else {
            System.out.println("[CACHE][INFO]: Cache hit for key " + key);
            MetricsUtil.recordCacheHit();
        }
        return result;
    }

    public void put(K key, V value) {
        mp.put(key, value);
    }
}

interface BlogService{
    Blog getBlogById(String id);
}

class BlogServiceImpl implements BlogService {
    public Blog getBlogById(String id) {
        try {
            Thread.sleep(30);
            System.out.println("[SERVICE][INFO] getBlogById returned successfully. id: " + id);
            MetricsUtil.recordServiceCall();
            // Simulate fetching blog title by ID
            return new Blog(id, "Blog Title " + id);
        } catch (InterruptedException e) {
            System.out.println("[SERVICE][WARN] getBlogById operation cancelled. id: " + id);
            return null;
        }
    }
}

// Cached implementation of BlogService to demonstrate cache stampede
class BlogServiceCachedImpl implements BlogService {
    private final BlogService service;
    private final InMemoryCache<String, Blog> cache = new InMemoryCache<>();

    public BlogServiceCachedImpl(BlogService service) {
        this.service = service;
    }

    public Blog getBlogById(String id) {
        if (cache.get(id) != null) {
            return cache.get(id);
        }
        Blog result = service.getBlogById(id);
        if (result != null) {
            cache.put(id, result);
        }
        return result;
    }
}

// Cached implementation of BlogService with
// request collapsing to prevent cache stampede
class BlogServiceCachedCollapsedImpl implements BlogService {
    private final BlogService service;
    private final InMemoryCache<String, Blog> cache = new InMemoryCache<>();

    private final ConcurrentHashMap<String, CompletableFuture<Blog>> inflightRequests = new ConcurrentHashMap<>();

    public BlogServiceCachedCollapsedImpl(BlogService service) {
        this.service = service;
    }

    public Blog getBlogById(String id) {
        Blog cached = cache.get(id);
        if (cached != null) {
            return cached;
        }

        CompletableFuture<Blog> future = inflightRequests.computeIfAbsent(id, key ->
            CompletableFuture.supplyAsync(() -> {
                Blog result = service.getBlogById(key);
                if (result != null) {
                    cache.put(key, result);
                }
                return result;
            }).whenComplete((result, ex) -> inflightRequests.remove(key))
        );
        return future.join();
    }
}

public class Main {
    public static void main(String[] args) {
        simulateConcurrentCalls();
    }

    public static void simulateSequentialCalls() {
        BlogService blogService = new BlogServiceCachedCollapsedImpl(new BlogServiceImpl());

        System.out.println(blogService.getBlogById("ABC").getTitle());
        System.out.println(blogService.getBlogById("BCD").getTitle());
        System.out.println(blogService.getBlogById("ABC").getTitle());
        System.out.println(blogService.getBlogById("ABC").getTitle());
        System.out.println(blogService.getBlogById("BCD").getTitle());

        System.out.println("\n================== METRICS ==================");
        MetricsUtil.printMetrics();
    }

    public static void simulateConcurrentCalls() {
        BlogService blogService = new BlogServiceCachedCollapsedImpl(new BlogServiceImpl());

        Runnable task = () -> {
            System.out.println("[" + Thread.currentThread().getName() + "] " + blogService.getBlogById("ABC").getTitle());
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);
        Thread thread3 = new Thread(task);

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\n================== METRICS ==================");
        MetricsUtil.printMetrics();
    }
}
