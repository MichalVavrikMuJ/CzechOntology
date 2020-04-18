# CzechOntology

The project aims to create an ontology of Czech words, by linking existing linguistic resources (mainly DeriNet 2.0 and underlying MorfFlex CZ) with the latest Wikipedia dump. Provided everything goes well, ontology shall be written in the RDF CFL language.

## Getting started

[Download `.vec` word2vec model of your preference](https://fasttext.cc/docs/en/crawl-vectors.html#models) to `/src/main/resources/fasttext`.

Loading word2vec model comes with great demands on memory, and so to avoid problems following arguments must be set in Command Line Options (AKA VM Arguments):
```
-Xms1024m
-Xmx10g
-XX:MaxPermSize=2g
```