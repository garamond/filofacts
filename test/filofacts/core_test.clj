(ns filofacts.core-test
  (:require [midje.sweet :refer :all]
            [filofacts.core :refer :all]))

(defn tmp-db []
  (open (str (System/getProperty "java.io.tmpdir")
             (System/currentTimeMillis))))

(facts "database operations"
       (let [db (tmp-db)]
         (meta (insert! db {:name "foo" :age 23})) => {:id 1 :rev 1}
         (meta (insert! db {:name "bar" :age 32})) => {:id 2 :rev 1}
         (count (find-all db)) => 2
         (let [foo (find-one db 1)]
           (meta (update! db 1 (assoc foo :name "baz")))) => {:id 1 :rev 2}
           (:name (find-one db 1)) => "baz"
           (count (.list db)) => 2
           (count (find-all db #(= (:name %) "baz"))) => 1))

(facts "load"
       (let [db (tmp-db)
             max 100]
         (dotimes [n max] (insert! db {:foo n}))
         (count (find-all db)) => max
         (:foo (find-one db max)) => (- max 1)))
