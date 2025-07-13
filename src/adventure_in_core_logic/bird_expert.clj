;; based on bird.pl from https://github.com/s-webber/prolog-expert-system

(ns adventure_in_core_logic.bird_expert
  (:require [clojure.core.logic :refer :all]
            [clojure.core.logic.pldb :as pldb]))

(pldb/db-rel bird name family color order 
             size flightype voice season country 
             head cheek eats flightprpfile tail throat)

(def bird-facts
  (pldb/db
    [bird 'laysan-albatross 'albatross 'white nil
    nil nil nil nil nil
    nil nil nil nil nil nil]
    [bird 'black-footed-albatross 'albatross 'dark nil
    nil nil nil nil nil
    nil nil nil nil nil nil]
    [bird 'fulmar nil nil 'tubenose
    'medium 'flap-glide nil nil
    nil nil nil nil nil nil]
    [bird 'whistling-swan 'swan nil nil
    nil nil 'muffled-musical-whistle nil nil
    nil nil nil nil nil nil]
    [bird 'trumpeter-swan 'swan nil nil
    nil nil 'loud-trumpeting nil nil
    nil nil nil nil nil nil]
    [bird 'canada-goose 'canada-goose nil nil
    nil nil  nil 'winter 'united-states
    'black 'white nil nil nil nil]
    [bird 'canada-goose 'canada-goose nil nil
    nil nil  nil 'summer 'canada
    'black 'white nil nil nil nil]
    [bird 'snow-goose 'goose 'white nil
    nil nil 'quack nil nil
    nil nil nil nil nil nil]
    [bird 'mallard 'duck 'mottled-brown nil
    nil nil nil nil nil
    nil nil nil nil nil nil]
    [bird 'pintail 'duck nil nil
    nil nil 'short-whistle nil nil
    nil nil nil nil nil nil]
    [bird 'turkey-vulture 'vulture nil     nil
    nil nil nil nil nil
    nil nil nil 'v-shaped nil nil]
    [bird 'california-condor 'vulture nil nil
    nil nil nil nil nil
    nil nil nil 'flat nil nil]
    [bird 'sparrow-hawk 'falcon nil nil
    nil nil nil nil nil
    nil nil 'insects nil nil nil]
    [bird 'peregrine-falcon 'falcon nil nil
    nil nil nil nil nil
    nil nil 'birds nil nil nil]
    [bird 'great-crested-flycatcher 'flycatcher nil nil
    nil nil nil nil nil
    nil nil nil nil 'long-rusty nil]
    [bird 'ash-throated-flycatcher 'flycatcher nil nil
    nil nil nil nil nil
    nil nil nil nil nil 'white]
    [bird 'barn-swallow 'swallow nil nil
    nil nil nil nil nil
    nil nil nil nil 'forked nil]
    [bird 'cliff-swallow 'swallow nil nil
    nil nil nil nil nil
    nil nil nil nil 'square nil]
    [bird 'purple-martin 'swallow 'dark nil
    nil nil nil nil nil
    nil nil nil nil nil nil]))


(pldb/with-db bird-facts
  (run* [q]
    (fresh [family color order
            size flightype voice season country
            head cheek eats flightprpfile tail throat] 
       (bird q family color order
               size flightype voice season country
               head cheek eats flightprpfile tail throat))))

(pldb/with-db bird-facts
  (run* [q]
     (fresh [family color order
             size flightype voice season country
             head cheek eats flightprpfile tail throat]
        (bird q family 'white order
                size flightype voice season country
                head cheek eats flightprpfile tail throat))))

(pldb/with-db bird-facts
  (run* [q]
     (fresh [family color order
        size flightype voice season country
        head cheek eats flightprpfile tail throat]
        (bird q 'albatross color order
             size flightype voice season country
             head cheek eats flightprpfile tail throat))))


;; this is supposed to return everything about 'fulmar but does not work
(pldb/with-db bird-facts
  (run* [q]
    (fresh [family color order
            size flightype voice season country
            head cheek eats flightprpfile tail throat]
      (bird 'fulmar family color order
                    size flightype voice season country
                    head cheek eats flightprpfile tail throat)
      (== q [family color order
             size flightype voice season country
             head cheek eats flightprpfile tail throat]))))