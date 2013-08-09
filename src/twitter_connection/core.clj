(ns twitter-connection.core
  (:use
   [twitter.oauth]
   [twitter.callbacks]
   [twitter.callbacks.handlers]
   [twitter.api.restful]
   [clojurewerkz.cassaforte.cql]
   [clojurewerkz.cassaforte.query])
  (:require [opennlp.nlp :as nlp]
   [opennlp.treebank :as tb]
	 [opennlp.tools.filters :as nlp-tools]
   [clojurewerkz.titanium.graph    :as tg]
	 [clojurewerkz.titanium.edges    :as te]
	 [clojurewerkz.titanium.vertices :as tv]
	 [clojurewerkz.titanium.types    :as tt]
	 [clojurewerkz.titanium.query    :as tq]
   [clojurewerkz.cassaforte.client :as cass]
   [clj-json.core :as json])
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

;; natural language processing
(def get-sentences (nlp/make-sentence-detector "models/en-sent.bin"))
(def tokenize (nlp/make-tokenizer "models/en-token.bin"))
;(def detokenize (nlp/make-detokenizer "models/english-detokenizer.xml"))
(def pos-tag (nlp/make-pos-tagger "models/en-pos-maxent.bin"))
(def chunker (tb/make-treebank-chunker "models/en-chunker.bin"))

(defn test-a
	[]
	(pos-tag (tokenize "Mr. Smith gave a car to his son on Friday.")))

(test-a)

;; Extract nouns and verbs
(nlp-tools/nouns (pos-tag (tokenize "Mr. Smith gave a car to his son on Friday.")))
(nlp-tools/verbs (pos-tag (tokenize "Mr. Smith gave a car to his son on Friday.")))

;; Categorizer, use Open NLP

;; Titanium, graph database using BercleyDB or Cassandra as storage engine
(def conf {;; Embedded cassandra settings
           "storage.backend" "berkeleyje"
           "storage.directory" "/tmp/graph"})
           ;;"storage.cassandra-config-dir" cs-dir
           ;; Embedded elasticsearch settings
           ;;"storage.index.search.backend" "elasticsearch"
           ;;"storage.index.search.directory" "/tmp/cassandra/elasticsearch"
           ;;"storage.index.search.client-only" false
           ;;"storage.index.search.local-mode" true})

(tg/open (System/getProperty "java.io.tmpdir"))

;; Create vertice
(tg/transact! (tv/create! {:name "Michael" :location "Europe"}))

;; Create edges
(tg/transact!
 (let [p1 (tv/create! {:title "ClojureWerkz" :url "http://clojurewerkz.org"})
       p2 (tv/create! {:title "Titanium"     :url "http://titanium.clojurewerkz.org"})]
   (te/connect! p1 :meaningless p2)
   (te/connect! p1 :meaningful  p2 {:verified-on "February 11th, 2013"})))

(tg/transact! 
 (tt/defkey :age Long {:indexed-vertex? true :unique-direction :out}))

(tg/transact! 
 (tv/to-map (tv/create! {:name "Zack"   :age 22})))

(tg/transact! 
 (tv/create! {:name "Trent"  :age 22}))

(tg/transact! 
 (tv/create! {:name "Vivian" :age 19}))

(tg/transact! (tv/to-map (first (tv/find-by-kv :age 22))))

(tg/transact!
  (doseq [x (first (tv/find-by-id :age 22))] (.toString x)))

(tv/id-of a) 

(tv/keys ab)

(tg/transact! (tv/get :name "Zack"))

;; Example of creating vertices, edges and fetching relations
(tg/transact!
     (let [from-node (tv/create! {:url "http://clojurewerkz.org/"})
           to-node   (tv/create! {:url "http://clojurewerkz.org/about.html"})
           created   (te/connect! from-node :links to-node)
           fetched   (te/find-by-id  (te/id-of created))]
     (print (map tv/to-map [from-node to-node created fetched]))
     (print "The URL:" (:url (tv/to-map from-node)))))       

(tg/transact!
  (println (count (tv/get-all-vertices)) (count (te/get-all-edges))))

(tg/transact!
  (tv/get-all-vertices))

;;;;;;;;;;;; Cassandra
(cass/connect! ["127.0.0.1"])

;;;;;;;;;;;; JSON
(json/generate-string {:name "Johan" :city "Lund"})

;;;;;;;;;;;; Sample data

;;;;;;;;;;;; Redis
;; Use redis for sessions


(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn -main
  []
  (println "Johan"))
