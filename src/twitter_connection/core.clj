(ns twitter-connection.core
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful])
  (:import
   (twitter.callbacks.protocols SyncSingleCallback)))

(def *app-consumer-key* "4VkEgZ9vr3HXLhAjg4dTg")
(def *app-consumer-secret* "ZZdFmrH1aZf8QJxv3eu67nWWdvmpTcvIJU1hzchyGs")
(def *user-access-token* "1238791760-ISbM7bJbqfzc5cLP5sExz3WdGfLCPn4PIEvscXt")
(def *user-access-token-secret* "U4R50ery5Pco7awigMCR39OKb2okbEzjsrjah3mYE")

(def my-creds (make-oauth-creds *app-consumer-key*
                                *app-consumer-secret*
                                *user-access-token*
                                *user-access-token-secret*))

(users-show :oauth-creds my-creds :params {:screen-name "gecemmo"})

(def apa (statuses-user-timeline :oauth-creds my-creds))
(def tweet (:text (first (:body apa))))
(filter (fn [x] (= (first x) \#)) (clojure.string/split tweet #" "))

(map (first (:body apa)) [:text :created_at])
; ((juxt :text :created_at) (first (:body apa)))



(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main
  []
  (println "Johan"))
