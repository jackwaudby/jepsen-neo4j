(ns jepsen.neo4j
  (:require [clojure.tools.logging :refer :all]
            [slingshot.slingshot :refer [try+]]
            [clojure.string :as str]
            [neo4clj.client :as nc]
            [jepsen [cli :as cli]
             [control :as c]
             [client :as client]
             [db :as db]
             [checker :as checker]
             [generator :as gen]
             [tests :as tests]]
            [jepsen.checker.timeline :as timeline]
            [jepsen.control.util :as cu]
            [knossos.model :as model]
            [jepsen.os.debian :as debian]))

;;; client
;;; model neo4j as a single node with 1 property that we read and write.
(defn r  [_ _]  {:type :invoke, :f :read, :value 5})
(defn w  [_ _]  {:type :invoke, :f :write, :value (rand-int 10)})

(defrecord Client [conn]
  client/Client
  (open! [this test node]
    (assoc this :conn (nc/connect (str "neo4j://" node ":7687") "neo4j" "neo4j")))

  (setup! [this test]
    ;; (info (nc/create-node! conn {:ref-id "p"
    ;;                              :labels [:person]
    ;;                              :props {:name "Jack"}}))
    )

  (invoke! [_ test op]
    (case (:f op)
      :read (assoc op
                   :type :ok,
                   :value (get-in (nc/find-node conn {:ref-id "n"
                                                :props {:name "Jack"}}) [:props :rating] ))
      :write (do
        (nc/update-props! conn {:props {:name "Jack"}} {:rating (:value op)})
        (assoc op :type :ok))
      )
    )

  (teardown! [this test])

  (close! [_ this]
    (nc/disconnect conn)))


(defn node-ip-port
  [node]
  (str node ":5000"))

(defn initial-discovery-members
  [test]
  (->> (:nodes test)
       (map (fn [node]
              (node-ip-port node)))
       (str/join ",")))


(def homedir "/home/azureuser" )
(def directory "/home/azureuser/neo4j-enterprise-4.4.0-dev")
(def logfile (str directory "/logs/neo4j.log"))
(def conffile (str directory "/conf/neo4j.conf"))

(defn db
  "Neo4j DB for a particular version."
  [version]
  (reify db/DB
    (setup! [_ test node]
      ;; (info node "installing neo4j" version)
      ;; (c/su
      ;;  (c/exec* (str "cd " homedir " && tar -xvf neo4j-enterprise-4.4.0-dev-unix.tar.gz"))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.mode/dbms.mode/g' " conffile))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.default_listen_address/dbms.default_listen_address/g' " conffile))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#causal_clustering.initial_discovery_members=localhost:5000,localhost:5001,localhost:5002/causal_clustering.initial_discovery_members=" (initial-discovery-members test) "/g' " conffile))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.default_advertised_address=localhost/dbms.default_advertised_address=" node "/g' " conffile))

      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.security.auth_enabled=false/dbms.security.auth_enabled=false/g' " conffile))

      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.routing.enabled=false/dbms.routing.enabled=true/g' " conffile))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.routing.listen_address/dbms.routing.listen_address/g' " conffile))
      ;;  (c/exec* (str "cd " homedir " && sed -i 's/#dbms.routing.advertised_address=:7688/dbms.routing.advertised_address=" node ":7688/g' " conffile))
      ;;  (info (c/exec* (str directory "/bin/neo4j start"))))
      ;; (Thread/sleep 50000)
      (info "done waiting"))

    (teardown! [_ test node]
      ;; (info (c/su (c/exec* (str directory "/bin/neo4j stop || true"))))
      ;; (info node "tearing down neo4j")
      ;; (c/su (c/exec :rm :-rf directory))
      )

    db/LogFiles
    (log-files [_ test node]
      [logfile])
    ))

(defn neo4j-test
  "Given an options map from the command line runner (e.g. :nodes, :ssh,
  :concurrency, ...), constructs a test map."
  [opts]
  (merge tests/noop-test
         {:pure-generators true
          :name "neo4j"
          :os debian/os
          :db (db "4.3.7")
          :client (Client. nil)
          :checker (checker/compose
                    {:linear (checker/linearizable
                              {:model (model/register)
                               :algorithm :linear})
                     :timeline (timeline/html)})
          :generator       (->> (gen/mix [r w])
                                (gen/stagger 1)
                                (gen/nemesis nil)
                                (gen/time-limit 15))
          }
         opts))

(defn -main
  "Handles command line arguments. Can either run a test, or a web server for
  browsing results."
  [& args]
  (cli/run! (merge (cli/single-test-cmd {:test-fn neo4j-test})
                   (cli/serve-cmd))
            args))
