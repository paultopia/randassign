(ns randassign.core
  (:gen-class)
  (:require
   [clojure.core.match :refer [match]]
   [clojure.string :refer [blank?]]
   [clojure.data.csv :refer [read-csv]]
   [clojure.data.json :refer [read-str write-str]]))

(defn vec-remove
  "remove elem in coll"
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn pick-unique
  "takes two collections, 'out' and 'in'. checks first item of in to see if out contains item. if not, adds to out and removes from in. otherwise goes to next item, etc.  in is an atom, because I can't figure out a good way to do this without mutability.
  will horribly break with an infinite loop if there are no valid items but I don't care.  returns both collections"
  ([in* out]
   (pick-unique 0 in* out))
  ([idx in* out]
   (let [in @in*
         pick (nth in idx)]
    (if-not (.contains out pick)
      (do (reset! in* (vec-remove in idx))
          (conj out pick))
      (recur (inc idx) in* out)))))

(defn walk-over-students-once [studs assgs]
  (map (partial pick-unique addgs) studs))
;; this isn't correct.  studs is a vector of maps, not a plain vector, I need to pluck the map out of each of them.  and also pass atom.  horrible.

(defn fill-seq [smaller bigger]
  (let [c (count bigger)]
    (vec (take c (cycle smaller)))))

(defn assign-students [students assignments num-assignments]
  (let [studs (apply merge (map #({% []}) students))
        assgs (fill-seq (shuffle assignments) studs)]))

(defn assign [students assignments num-assignments]
  (str students " + " assignments))
;; stub/test

(defn stripblank [v]
  (vec (remove #(blank? %) v)))

(defn process-json [filename student-identifier assignment-identifier num-assignments]
  (let [json (read-str (slurp filename))
        students (stripblank (json student-identifier))
        assignments (stripblank (json assignment-identifier))]
    (assign students assignments num-assignments)))

(defn pick-column
  "assumes column identifiers are in header and are unique. else will go boom"
  [identifier columns]
  (let [column (first(vec (filter #(= identifier (first %)) columns)))]
    (-> column rest stripblank)))

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

