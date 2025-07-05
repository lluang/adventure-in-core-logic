(ns adventure_in_core_logic.nani
  (:refer-clojure :exclude [reify inc ==])
  (:require [clojure.core.logic :as l]
            [clojure.core.logic.pldb :as pldb]))

(pldb/db-rel room rtype)
(pldb/db-rel location thing room)
(pldb/db-rel door room1 room2)
(pldb/db-rel edible thing)
(pldb/db-rel tastes_yucky thing)
(pldb/db-rel turned_off thing)

(def nani_facts
  (pldb/db
   [room 'kitchen]
   [room 'office]
   [room 'hall]
   [room 'dining_room]
   [room 'cellar]

   [location 'desk 'office]
   [location 'apple 'kitchen]
   [location 'flashlight 'desk]
   [location 'washing_machine 'cellar]
   [location 'nani 'washing_machine]
   [location 'broccoli 'kitchen]
   [location 'crackers 'kitchen]
   [location 'computer 'office]

   [door 'office 'hall]
   [door 'kitchen 'office]
   [door 'hall 'dining_room]
   [door 'kitchen 'cellar]
   [door 'dining_room 'kitchen]

   [edible 'apple]
   [edible 'crackers]

   [tastes_yucky 'broccoli]

   [turned_off 'flashlight]))
;;(pldb/db-rel here room)
;;(pldb/db-fact here 'kitchen)
(def here (atom 'kitchen))

(defn istrue [vfact var]
  (if (empty? (l/run 1 [q] (vfact var))) 'no 'yes))

(pldb/with-db nani_facts 
  (istrue room 'office))
(pldb/with-db nani_facts 
  (istrue room 'attic))

(pldb/with-db nani_facts 
  (l/run 1 [q] (location 'apple 'kitchen)));;_.0 -> yes

(pldb/with-db nani_facts 
  (l/run* [q] (room q)))

(pldb/with-db nani_facts 
  (l/run* [q] (l/fresh [Thing Place] (l/== q [Thing Place]) (location Thing Place))))

;;compound queries
(pldb/with-db nani_facts 
  (l/run* [q] (location q 'kitchen) (edible q)))
;;(crackers apple)
(pldb/with-db nani_facts 
  (l/run* [q] (l/fresh [R T] (door 'kitchen R) (location T R) (l/== q [R T]))))

;; Everything in kitchen
;;(doseq [i (l/run* [q] (location q 'kitchen))] (println i))

;;rules
(defn where_food [x y]
  (l/all (location x y)
       (edible x)))

(pldb/with-db nani_facts 
  (l/run* [q] (where_food q 'kitchen)))

(pldb/with-db nani_facts 
  (l/run* [Thing] (where_food Thing 'dining_room)))

(pldb/with-db nani_facts 
  (l/run 1 [q] (where_food 'apple 'kitchen)));; yes/no

(pldb/with-db nani_facts 
  (l/run* [q] (l/fresh [Thing Room] (where_food Thing Room) (l/== q [Thing Room]))))

(defn where_food [x y]
  (l/conde
    ((l/all (location x y)
          (edible x)))
    ((l/all (location x y)
          (tastes_yucky x)))))

(pldb/with-db nani_facts 
  (l/run* [q] (where_food q 'kitchen)))


(defn connect [x y]
     (l/conde
     ((door x y))
     ((door y x))))

(pldb/with-db nani_facts 
  (l/run 1 [q] (connect 'kitchen 'office)))

(pldb/with-db nani_facts 
  (l/run 1 [q] (connect 'office 'kitchen)))

(pldb/with-db nani_facts 
  (l/run* [q] (l/fresh [X Y] (connect X Y) (l/== q [X Y]))))

(defn list_things [Place]
  (l/run* [q] (location q Place)))

(defn list_connections [Place]
  (l/run* [q] (connect Place q)))

(defn look []
  (let [Place @here]
    (println "You are in the" Place)
    (println "You can see: ")
    (doseq [i (list_things Place)] (println "  " i))
    (println "You can go to: ")
    (doseq [i (list_connections Place)] (println "  " i))))

(defn look_in [where]
  (l/run* [q] (location q where)))

;;arithmetic -> this is a lisp dialect :D
;;(def some_arithmetic (+ 1 (/ 280 (* 7 4)))

;;Managing data
;;We are gonna use Clojure's Atoms for storing the current location: here

(defn can_go [Place]
  (if ((comp not empty?) (l/run* [q] (connect @here Place)))
    true
    (println "You can't get there form here.")))

;;true / nil
(pldb/with-db nani_facts (can_go 'office))
(pldb/with-db nani_facts (can_go 'hall))

;; Clojure solution (we use an atom insted of deleting the pldb/db-fact here and refreshing it)
(defn move [Place]
  (reset! here Place))

(defn goto [Place]
  (if (can_go Place)
    (move Place))
  (look))

(pldb/with-db nani_facts 
  (goto 'kitchen))

(defn can_take [Thing]
  (if ((comp not empty?) (l/run* [q] (location Thing @here)))
    true
    (println "There is no" Thing "here.")))

(pldb/db-rel have x)

(defn take_object [x]
  (pldb/db-retraction location x @here)
  (pldb/db-fact have x)
  (println "taken"))

;; take is reserved
(defn take_now [x]
  (if (can_take x)
    (take_object x)))

(defn put [x]
  (if ((comp not empty?) (l/run* [q] (have x)))
    (do (pldb/db-retraction have x) (pldb/db-fact location x @here) (println "Putting" x "down"))
    (println "You don't have" x)))

(defn inventory []
  (println "You have: ")
  (doseq [i (l/run* [q] (have q))] (println i)))

;; TODO: Excercise 4 5


(pldb/with-db nani_facts 
  (l/run* [q] (location 'flashlight 'office))) ;; ()



(def nani-facts1
  (-> nani_facts
      (pldb/db-fact location 'envelope 'desk)
      (pldb/db-fact location 'stamp 'envelope)
      (pldb/db-fact location 'key 'envelope)))

(defn is_contained_in [T1 T2]
  (l/conde
    ((location T1 T2))
    ((l/fresh [X] (location X T2)
                (is_contained_in T1 X)))))

(pldb/with-db nani-facts1
  (goto 'office)
  (look))