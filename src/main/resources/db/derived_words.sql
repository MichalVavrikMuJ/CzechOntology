create or replace TYPE "WORD_VECTOR" AS VARRAY(300) OF DOUBLE PRECISION;
DROP TABLE "NEURONKY"."DERIVED_SAMPLES";

CREATE TABLE "NEURONKY"."DERIVED_SAMPLES" (
	"ID" FLOAT NOT NULL,
	"DERIVED_FROM" FLOAT NOT NULL, 
	"WORD" VARCHAR2(3999) NOT NULL,
	"POSITIVE_OR_NEGATIVE" NUMBER(1,0) NOT NULL,
    "WORD_VECTOR" WORD_VECTOR NOT NULL
	);