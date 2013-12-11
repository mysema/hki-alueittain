(defproject hki-alueittain "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [org.clojure/tools.logging "0.2.6"]
                 [org.slf4j/slf4j-api "1.6.1"]
                 [org.slf4j/slf4j-log4j12 "1.6.1"]
                 [hiccup "1.0.1"]
                 [dk.ative/docjure "1.6.0"]  ]
  :plugins [[lein-ring "0.8.6"]]
  :ring {:handler hki-alueittain.core/app}
  :profiles {:dev {:dependencies [[midje "1.5.1"]
                                  [ring-server "0.2.8"]]
                   :plugins [[lein-midje "3.1.0"]]}})
