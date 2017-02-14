#open-cata project
This rep contains notes about my MSc Computer engineering thesis about administrative open-data categorization with text mining techniques. Open-CATA is a text classifier (Bayesian classifier) designed to assign a category Eu-Data-Themes for each dataset (open-data) of the Open Data Hub Italy portal (http://www.sciamlab.com/opendatahub).

Open-CATA is optimized for the categorization of open-data using the data model DCAT-AP, it combines the following techniques to get the best results:

1.Multiclass task with Multinomial, Bernoulli (posteriori probability)
2.Multilabel task with Kullback-Leibler divergence (posteriori probability)
3.Chi-square, mutual information feature selection
4.Outlier & incremental training with gain-information & 'CKAN-API' rules

##UML class diagram & packages

![Alt text](th/img/UMLclassi.png?raw=true "UML")

```
packages:

com.sciamlab.it.eurovoc
contains utilities to manage EuroVoc thesaurus

com.sciamlab.it.acquis
contains utilities to handle from xml data to database: 
xquery, filesystem, ...

com.sciamlab.it.cata
contains the implementation of text mining algorithms:
classifiers, feature selection, feature extraction, 
evaluators and the training set model, CKAN-API, ...
```

##resources
* italian open-data hub, http://www.sciamlab.com/opendatahub
* JRC-ACQUIS Multilingual Parallel Corpus, Version 3.0, http://optima.jrc.it/Acquis/JRC-Acquis.3.0/corpus
* stemming by Snowball, https://github.com/snowballstem
* EuroVoc 4.5: the EU's multilingual thesaurus, http://eurovoc.europa.eu
