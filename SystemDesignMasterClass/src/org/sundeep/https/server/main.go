package main

import (
    "fmt"
    "log"
    "net/http"
)

func main() {
    http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
        w.Header().Set("Content-Type", "text/plain; charset=UTF-8")
        w.WriteHeader(http.StatusOK)
        fmt.Fprintf(w, "Hello, World!")
    })

    port := ":8080"
    log.Printf("Starting HTTPS server on https://localhost:%s\n", port)

    err := http.ListenAndServeTLS(port, "localhost+2.pem", "localhost+2-key.pem", nil)
    if err != nil {
        log.Fatalf("Failed to start HTTPS server: %v", err)
    }
}