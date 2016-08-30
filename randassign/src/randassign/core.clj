(ns randassign.core
  (:gen-class)
  (:require
   [clojure.core.match :refer [match]]
   [clojure.data.csv :refer [read-csv]]
   [clojure.data.json :refer [read-str write-str]]))

(defn assign [students assignments num-assignments]
  (str students " + " assignments))
;; stub/test

(defn process-json [filename student-identifier assignment-identifier num-assignments]
  (let [json (read-str (slurp filename))
        students (json student-identifier)
        assignments (json assignment-identifier)]
    (assign students assignments num-assignments)))

(defn pick-column
  "assumes column identifiers are in header and are unique. else will go boom"
  [identifier columns]
  (let [column (first(vec (filter #(= identifier (first %)) columns)))]
   (rest column)))

(defn slurp-csv [filename]
  (apply mapv vector(read-csv (slurp filename))))

(defn process-csv [filename student-identifier assignment-identifier num-assignments]
  (let [cols (slurp-csv filename)
        students (pick-column student-identifier cols)
        assignments (pick-column assignment-identifier cols)]
    (assign students assignments num-assignments)))

(defn -main
  "do all the things."
  [filename student-identifier assignment-identifier num-assignments]
  (println
   (match [(vec (take-last 3 filename))]
          [[\s \o \n]] (process-json filename student-identifier assignment-identifier num-assignments)
          [[\c \s \v]] (process-csv filename student-identifier assignment-identifier num-assignments)
          :else (str "saw: " (take-last 3 filename) " unable to guess file type. please pass .csv or .json file. Syntax: randassign FILE STUDENTS-IDENTIFIER ASSIGNMENTS-IDENTIFIER NUM-ASSIGNMENTS-PER-STUDENT"))))

