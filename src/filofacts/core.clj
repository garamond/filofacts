(ns filofacts.core
  (:require [clojure.java.io :refer [file]]
            [clojure.edn :as edn]))

(defn- ann [doc id rev]
  (with-meta doc {:id id :rev rev}))

(defn- open-doc [db id]
  (let [doc-dir (file db (str id))
        rev (last (sort (map str (.list doc-dir))))]
    (ann (edn/read-string (slurp (file doc-dir rev)))
         id rev)))

(defn- list-docs [dir]
  (sort (map #(Long. (str %)) (.list (file dir)))))

(defn- write-doc! [db-path id doc]
  (let [rev (inc (or (last (list-docs (file db-path (str id)))) 0))
        doc-dir (file db-path (str id))]
    (.mkdirs doc-dir)
    (spit (file doc-dir (str rev)) doc)
    (ann doc id rev)))

(defn open [path]
  (let [dir (file path)]
    (or (.isDirectory dir)
        (.mkdirs dir))
    dir))

(defn find-one [db id]
  (open-doc db id))

(defn find-all
  ([db pre]
     (filter pre (find-all db)))
  ([db]
     (map #(find-one db %) (list-docs db))))

(defn insert! [db doc]
  (let [id (inc (or (last (list-docs db)) 0))]
    (write-doc! db id doc)))

(defn update! [db id doc]
  (write-doc! db id doc))
