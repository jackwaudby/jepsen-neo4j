(ns jepsen.neo4j
  (:require [clojure.tools.logging :refer :all]
            [clojure.string :as str]
            [neo4clj.client :as client]
            [jepsen [cli :as cli]
             [control :as c]
             [db :as db]
             [tests :as tests]]
            [jepsen.control.util :as cu]
            [jepsen.os.debian :as debian]))

(def dir "/opt/neo4j")
(def logfile (str dir "/logs/neo4j.log"))

(defn db
  "Neo4j DB for a particular version."
  [version]
  (reify db/DB
    (setup! [_ test node]
      (info node "installing neo4j" version)
      (c/su
       (let [url (str "http://dist.neo4j.org/neo4j-community-" version "-unix.tar.gz")]
         (cu/install-archive! url dir)))
      (info (c/su (c/exec* (str dir "/bin/neo4j start"))))
      (Thread/sleep 10000))

    (teardown! [_ test node]
      (info (c/su (c/exec* (str dir "/bin/neo4j stop || true"))))
      (info node "tearing down neo4j")
      (c/su (c/exec :rm :-rf dir)))

    db/LogFiles
    (log-files [_ test node]
      [logfile])))



(defn neo4j-test
  "Given an options map from the command line runner (e.g. :nodes, :ssh,
  :concurrency, ...), constructs a test map."
  [opts]
  (merge tests/noop-test
         {:pure-generators true
          :name "neo4j"
          :os debian/os
          :db (db "4.3.7")}
         opts))

(defn -main
  "Handles command line arguments. Can either run a test, or a web server for
  browsing results."
  [& args]
  (cli/run! (merge (cli/single-test-cmd {:test-fn neo4j-test})
                   (cli/serve-cmd))
            args))
