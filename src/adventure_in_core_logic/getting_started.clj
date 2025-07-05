(ns adventure_in_core_logic.getting_started
  (:refer-clojure :exclude [reify inc ==])
  (:require [clojure.core.logic :as l]
        [clojure.core.logic.pldb :as pldb]))

(defn istrue [vfact var]
  (if (empty? (l/run 1 [q] (vfact var))) 'no 'yes))

(pldb/db-rel person x)

(defn mortal [x] (person x))

(def people
  (pldb/db 
   [person 'Socrates]
   [person 'Plato]))

(pldb/with-db people 
  (istrue mortal 'Socrates))

(pldb/with-db people 
    (l/run* [q] (mortal q)))

(pldb/with-db people
    (l/run* [q] (mortal 'Socrates)))

;;(println "hello world")

(def morepeople 
  (-> people
      (pldb/db-fact person 'Zeno)
      (pldb/db-fact person 'Arisotle)))

(pldb/with-db morepeople
  (l/run* [q] (mortal q)))

(defn mortal-report []
  (println "Known mortals are:")
  (doseq [i (pldb/with-db morepeople (l/run* [q] (mortal q)))] (println i)))

(mortal-report)

(pldb/db-rel customer)

(def customerlist
  (pldb/db
    [customer "John Jones" 'Boston 'good_credit]
    [customer "Sally Smith" 'Chicago 'good_credit]))

(pldb/with-db customerlist
  (l/run* [name name city credit] (customer name city credit)))

(pldb/db-rel window name ux uy lx ly)

(def windowlist
  (pldb/db
      [window 'main 2 2 20 72]
      [window 'errors 15 40 20 78]))

(pldb/with-db windowlist 
  (l/run* [name ux uy lx ly] (window name ux uy lx ly)))

(pldb/db-rel disease name prop)

(def diseaselist
  (-> pldb/empty-db
     (pldb/db-fact disease 'plague 'infectious)))

(pldb/with-db diseaselist
  (l/run* [name prop] (disease name prop)))
