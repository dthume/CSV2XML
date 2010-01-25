(ns com.dthume.csv2xml
  (:gen-class)
  (:import [java.io FileWriter])
  (:require [com.davidsantiago.csv :as csv]
            [clojure.contrib.prxml :as prxml]))

(defn make-xml
  [input output]
  (with-open [w (FileWriter. output)]
    (binding [*out* w]
      (let [[column-names & rows] (csv/parse-csv (slurp input))
            col->xml (fn [n d] [:col {:name n} d])
            row->xml (fn [row] [:row (map col->xml column-names row)])]
        (prxml/prxml
         [:decl! {:version "1.0" :encoding "UTF-8"}]
         (into [:csv] (map row->xml rows)))))))

(defn -main [& [input output]]
  (make-xml input output))
