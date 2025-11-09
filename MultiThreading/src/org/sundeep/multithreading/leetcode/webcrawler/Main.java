package org.sundeep.multithreading.leetcode.webcrawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 *     public List<String> getUrls(String url) {}
 * }
 */
interface HtmlParser {
    List<String> getUrls(String url);
}

// Multi-threaded
class Solution {
    private boolean hasSameHostname(String url1, String url2) {
        if (!url1.startsWith("http://") || !url2.startsWith("http://")) {
            return false;
        }
        int i = 7, j = 7;
        while (i < url1.length() && j < url2.length() && url1.charAt(i) == url2.charAt(j)) {
            if (url1.charAt(i) == '/' && url2.charAt(j) == '/') {
                return true;
            }
            i++;
            j++;
        }
        return (i >= url1.length() && j >=url2.length()) || (i >= url1.length() && url2.charAt(j) == '/') || (url1.charAt(i) == '/' && j >= url2.length());
    }

    private Map<String, Boolean> visitedUrls = new HashMap<>();
    private List<String> result = new Vector<>();

    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        visitedUrls.put(startUrl, true);
        result.add(startUrl);
        crawlUtil(startUrl, htmlParser);
        return result;
    }

    private void crawlUtil(String startUrl, HtmlParser htmlParser) {
        List<String> subUrls = htmlParser.getUrls(startUrl);

        List<Thread> threads = new ArrayList<>();

        for (String url: subUrls) {
            if (!hasSameHostname(startUrl, url)) {
                continue;
            }
            if (visitedUrls.containsKey(url)) {
                continue;
            }

            visitedUrls.put(url, true);
            result.add(url);

            Thread cur = new Thread(() -> {
                crawlUtil(url, htmlParser);
            });

            threads.add(cur);
            cur.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class Main {
}
