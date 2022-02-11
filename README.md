# Wikipedia-based Explicit Semantic Analysis

## ESA

Explicit Semantic Analysis (ESA) is a technology used to represent texts as vectors into a document corpus space. The document corpus is traditionally Wikipedia. These vectors can then be used to compare documents for similarity.

ESA was written by Gabrilovich and ? in 2006 and far surpassed the previous approaches to document similarity (LSA). To our knowledge, no further methods of document similarity exist which are better than ESA, though some improvements can be found by augmenting ESA with other technologies (such as word2vec). 

This implementation of ESA was written by Sheldon Juncker (sheldonjuncker@gmail.com) and the DreamCloud Team (dreamcloud.app/about).

We created our ESA tool to follow the original ESA implementation as defined by Gabrilovich in his 2007 paper. (add in paper reference).
Using the 2005 Wikipedia XML dump used in this paper, we were able to get Spearman/Pearson scores of 0.75/0.72, and with 2006 we got 0.75/0.73 which are all within 0.01 of the historical ones--and our 2005 numbers are slightly higher than those in the original paper.


## License

This software is provided under the terms of the MIT license, which pretty much lets you do anything you want with the software provided you leave our copyright notice and don't hold us responsible for any liability.

## Usage

ESA can be used as a library or as a stand-alone tool. There are a variety of different Maven packages that we host here on GitHub which can be used or combined in various ways:

- `esa-core`: The core ESA implementation including text analysis, document preprocessing, and vectorization.
- `esa-score`: The ESA scoring library which contains a set of scoring algorithms (various versions of TF-IDF and BM25) and file system writing and reading tools for working with written score indexes.
- `esa-wiki`: A command line tool and library for processing Wikipedia XML dumps and generating a final preprocessed corpus taking into account: templates, variables, term counts, link counts, English stopwords and dictionary words, rare words, and more.
- `esa-server`: A simple HTTP server for vectorizing documents and comparing text similarity.
- `esa-tuner`: A tool for verifying the accuracy of an ESA implementation using the WordSim-353 dataset with Spearman and the LP-50 document dataset with Pearson. It also includes a tool for fine-tuning an ESA implementation.

Each of these libraries includes a few CLI tools for creating the various configuration classes from CLI options. This is useful if you want to create a CLI tool for interfacing with the library.

## Quick start

### 1) Install Java

If you haven't set up Java on your computer, you'll need that.

**On Mac**
Using Homebrew,
`brew install java`
then install Maven:
`brew install maven`
Also, you'll need the Java Development Kit:
[https://www.oracle.com/java/technologies/downloads/#jdk17-mac](https://www.oracle.com/java/technologies/downloads/#jdk17-mac)
Use the `Arm 64 DMG Installer` or `x64 DMG Installer`, depending on your system.

### 2) Install packages using Maven

In the home folder of the repo, run:
`mvn package`

### 3) Make folders for Wikipedia data

In the home folder of the repo:
`mkdir index` (for the Lucene index)
`mkdir enwiki` (to store your English Wikipedia download. You could use a different folder name and then different commands if you like).

### 4) Download a Wikipedia dump

A list of all available database dumps is available here: <https://dumps.wikimedia.org/backup-index-bydb.html>.

Click on a Wikipedia version, it's good to start with a Simple Wikipedia version if you're using English:

![image](https://user-images.githubusercontent.com/14936307/145384562-2431a7d5-bd36-454c-8779-241414e1f5a9.png)

On the next page, choose a download which contains all current articles without history, such as this:

![image](https://user-images.githubusercontent.com/14936307/145387013-26238b20-8be5-4803-9775-281231ac1c45.png)

### 5) Put the dump in the folder

Take your downloaded Wikipedia database dump (a zipped file in .bz2 format, e.g. `simplewiki-20211201-pages-articles-multistream.xml.bz2`), and put it in the folder you just made, e.g. `enwiki`.

### 6) Build the index

This can take some time, depending on your system:
On Mac:

- Make the `esa.sh` file executable: `chmod +x esa.sh`
- Run the script: `./esa.sh --preprocess enwiki/simplewiki-20211201-pages-articles-multistream.xml.bz2 index/2021 --title-exclusion [regex1 rege2 ...]` (Make sure you reference the dump file you just put in the folder)

On Windows:
`./esa.sh --preprocess enwiki/simplewiki-20211201-pages-articles-multistream.xml.bz2 index/2021 --title-exclusion [regex1 rege2 ...]`


### Indexing



### Analyzing



## References
