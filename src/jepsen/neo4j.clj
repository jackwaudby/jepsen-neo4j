(ns jepsen.neo4j
 (:require [neo4clj.client :as client]
            [jepsen.cli :as cli]
            [jepsen.tests :as tests]))

(def conn (client/connect "bolt://localhost:7687" "neo4j" "admin"))

(defn neo4j-test
  "Given an options map from the command line runner (e.g. :nodes, :ssh,
  :concurrency, ...), constructs a test map."
  [opts]
  (merge tests/noop-test
         {:pure-generators true}
         opts))

(defn -main
  "Handles command line arguments. Can either run a test, or a web server for
  browsing results."
  [& args]
  (cli/run! (merge (cli/single-test-cmd {:test-fn neo4j-test})
                   (cli/serve-cmd))
            args))
