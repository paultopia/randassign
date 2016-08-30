(defproject randassign "0.1.0-SNAPSHOT"
  :description "simple cli to randomly assign assignments to students"
  :url "https://github.com/paultopia/randassign"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.csv "0.1.3"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/core.match "0.3.0-alpha4"]]
  :main ^:skip-aot randassign.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
