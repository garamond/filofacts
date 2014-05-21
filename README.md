filofacts
=========

File-based document store for Clojure

## Overview

For small, throw-away projects or prototypes (such as [reading-list](http://github.com/garamond/reading-list)), the ceremony of setting up a database to persist state is often disproportionate. In-memory databases, on the other hand, are too fragile and fleeting, suitable only for testing environments. Enter `filofacts`, a file-based document store.

## Installation

Add the following dependency to your `project.clj` file:

	[filofacts "0.1.0"]

## Usage

	(ns test
	  (:require filofacts.core :as ff))
	
	; open database
	(def db (ff/open "/tmp/friends"))
	
	; save document
	; stored in /tmp/friends/1/1
	(def alice
	  (ff/insert! db {:name "Alice"
	                  :age 31
	                  :interests #{:travel :friends :beauty}}))

	; save document
	; stored in /tmp/friends/2/1
	(def bob
	  (ff/insert! db {:name "Bob"
	                  :age 29
	                  :interests #{:technology :football :cars}})) 
    
    ; document id and revision stored in metadata
    (println (meta alice)) ; => {:id 1 :rev 1}
    (println (meta bob))   ; => {:id 2 :rev 1}

	; update document
	; stored in /tmp/friends/2/2
	(def bob2
	  (ff/update! db (:id (meta bob)) {:name "Bob"
	                                   :age 23
	                                   :interests #{:technology :football :music}}))

    (println (meta bob2))   ; => {:id 2 :rev 2}
    
    ; retrieve latest rev of a document
	(def res (ff/find db (:id (meta bob2))))
	(println (:age res)) ; => 23	