(defproject twitter-connection "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [
	[org.clojure/clojure "1.4.0"]
  [clojure-opennlp "0.3.1"]
  [clojurewerkz/titanium "1.0.0-beta1"]
  [clojurewerkz/cassaforte "1.1.0"]
  [org.apache.httpcomponents/httpclient "4.1.2"]
  [org.clojure/clojure-contrib "1.2.0"]
  [clj-json "0.2.0"]
	[twitter-api "0.7.4"]]
  :main twitter-connection.core
  :plugins [[lein2-eclipse "2.0.0"]])
