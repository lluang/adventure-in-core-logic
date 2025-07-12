(ns adventure-in-core-logic.getting_started
  (:refer-clojure :exclude [reify inc ==])
  (:use [clojure.core.logic :as l]
        [clojure.core.logic.pldb :as pldb]))


(defn istrue [vfact var]
  (if (empty? (run 1 [q] (vfact var))) 'no 'yes))



(pldb/db-rel person x)

(defn mortal [x] (person x))

(def person_facts
  (pldb/db
   [person 'Socrates]
   [person 'Plato]
   ))


;;(run* [q] (mortal 'Socrates))
(pldb/with-db person_facts 
(istrue mortal 'Socrates))

(pldb/with-db person_facts
  (l/run 1 [q] (mortal q)))

;;(println "hello world")
(def more_person_facts
  (-> person_facts
      (pldb/db-fact person 'Zeno)
      (pldb/db-fact person 'Aristotle)
      ))


(defn mortal-report []
  (println "Known mortals are:")
  (pldb/with-db more_person_facts
    (l/run* [q] (mortal q))))

  ;  (doseq [i (l/run* [q] (mortal q))] (println i))))

(mortal-report)


(pldb/db-rel customer name city credit)

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
