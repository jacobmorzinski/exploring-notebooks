# exploring-notebooks

A exploration of some programming notebook systems, including Jupyter, MS Polyglot, and Clojure Clerk

## Motivation

I came across [a video](https://www.youtube.com/watch?v=_QnbV6CAWXc&t=1298s) demonstrating using [F#](https://fsharp.org/) for data visualization.  I wanted to (1) test the technology myself, (2) compare against a classic [ IPython ](https://ipython.org/)/[ Jupyter ](https://jupyter.org/) notebook, and (3) explore whether [Clojure](https://clojure.org/) has any similar tech.

## F#

At [21m38s in the video "F# as a Better Python"](https://www.youtube.com/watch?v=_QnbV6CAWXc&t=1298s) (NDC Oslo 2020), Phillip Carter shares a demonstration of using F# in a Dotnet Interactive Notebook (1).  He downloads data, selects part of the data, and then plots the data.

(1) [Dotnet Interactive](https://github.com/dotnet/interactive/) Notebooks have been renamed to Polyglot Notebooks.  One way to run them is by using a [Polyglot Notebooks extension](https://marketplace.visualstudio.com/items?itemName=ms-dotnettools.dotnet-interactive-vscode) for Visual Studio Code.

### Steps

* In VS Code, add the extension Polyglot Notebooks
  * ...it requires .Net SDK 7.  It was nice enough to give me a clear message and a download link.
* Use the palette command `Polyglot Notebook: Create new blank notebook`
  * Create a ".dib" file (is that "dotnet interactive book"?).
  * Choose a default language - I choose F#.
  * Your new notebook file is Untitled, you should save it and give it a name.  Following the video, I named my file "covid.dib"
* The rest of the process generally involved following the video.
* Remember that you can press Shift-Return or Control-Return to evaluate the active notebook cell.
* Some first thoughts: there are some rough spots.
  * I was surprised at how slow the main data processing loop is.  I suspect the code is doing un-needed work, but for the first steps I am keeping it the same as the demo video.
  * I also noticed that the final Plotly chart has poor tooltips on mouseover.  Again, for now I am keeping it the same as the demo video.

### F# Summary

Overall, the notebook worked, and was sucessfully able to save data in a dataframe and visualze the data.

## Python

It seems fair to compare with classic IPython, using Jupyter and Pandas.

### Steps

* Create a virtualenv by running `python -m venv venv`
  * `-m venv` loads the module "venv"
  * the next `venv` is the name of the folder where the virtualenv wil be stored
* Activate the venv
  * On Windows, the command is:  
    `. .\venv\Scripts\Activate.ps1`
* Prepare a "requirements.txt" file with desired packages:
```
jupyter
pandas
numpy
plotly
```
* In the activated venv, use `pip install -r requirements.txt` to install the packages.
  * It may take a while to download and/or build the packages.
* When installations are done, you can create a ipynb file in VS Code.
  * I used the Palette command `Create: New Jupyter Notebook`
  * Give it a name and save it.
  * Click the corner menu to try to choose the Python environment - choose the new "venv" that you created.
  * VS Code did not find my "venv", I had to quit and re-open VS Code from insde the venv in order for it to find the venv.
* Once VS Code can connect to the IPython kernel in your venv, you can start making your notebook.

### Python Summary

I know python better than I know F#, and I was more comfortable with the Python process.  I did struggle with installing packages - one of the binary packages compiles itself with Rust, and failed to compile until I noticed the error message and upgraded my Rust compiler version.

## Clojure

Doing the same task using Clojure was a rough adventure.  I have seen Clojure fans say that they wish more Clojure was used for data science, but I had challenges:

* Finding the right libraries was a challenge.
* Learning how to use them was a challenge.

In the end I did find some good libraries, but learning how to combine the pieces took hours of work.


### Reference dump:

This talk about Clojure data science has a slide at 5m9s that recommends Clojure library equivalents of python or R libraries:

https://www.youtube.com/watch?v=BxVtQM6FPHU&t=309s

It also links to a a "library resources" page at [Scicloj](https://scicloj.github.io/), a community promoting the use of Clojure for data-centric computing:

https://scicloj.github.io/docs/resources/libs/

I took note that two options listed at Calva Notebooks and Clerk

* Calva Notebooks
  * https://calva.io/notebooks/
* Clerk
  * https://github.com/nextjournal/clerk

### Clojure quick start

Starting a new Clojure project is still intimidating because you need to have the right files in the right place.  Here are some notes:

https://gist.github.com/Saikyun/7c3da5584145cbb0667058017cd4d667


### Calva Notebook

The Calva Notebook page has prominent warnings that Calva Notebooks are new, experimental, and may lose data.

The Calva command to "open as notebook" did not work well for me - it did not find my repl.  Maybe I did something wrong, but I decided to try Clerk.

### Clerk

[Clerk](https://clerk.vision/) \(https://clerk.vision/) is a bit of a different way of doing things.  Instead of evaluating your file a cell at a time, Clerk evaluates every expression (every clojure "form") in the file, and shows the results in a browser (or you can save it to a static file).  The behavior that it can re-run the whole file took me a while to learn.  Think about what this means if some of your commands are slow, or take actions like downloading a file.

There are some subtleties - Clerk uses a cache to reduce the number of times that it needs to re-run each statement.  If the statment's result is already cached, Clerk will not re-run the statment.

More details about using Clerk are in the [Book of Clerk](https://book.clerk.vision/) \(https://book.clerk.vision/)

#### Clojure Data Frames (Dataset)

The Scicloj group pointed to a new library: "dataset"

https://github.com/techascent/tech.ml.dataset

It looks quite nice and powerful.  However, it was a stuggle for me to remember enough Clojure to figure how to select and reshape data.  This is a case where being unfamiliar with a language slowed me down.

I found these pages when I was trying to figure out how to do things:
* https://github.com/genmeblog/techtest/wiki/Summary-of-functions
* https://github.com/genmeblog/techtest/blob/master/src/techtest/datatable_dplyr.clj
* https://techascent.github.io/tech.ml.dataset/walkthrough.html

I skipped this for now:
* https://github.com/scicloj/tablecloth


### Clojure Steps

* create a file "deps.edn"
* add content to the "deps.edn" - telling it that code is in "src", and that you depend on libraries and need them downloaded.
```clojure
{:paths ["src"]
 :deps {io.github.nextjournal/clerk {:mvn/version "0.12.707"}
        techascent/tech.ml.dataset {:mvn/version "7.000-beta-18"}}}
```
* Create a folder "src" and a subfolder "clojure_datavis"
* Create a file src/clojure_datavis/core.clj
* Add a namespace to the core.clj file.  Note that the namespace uses a dash "-" in the place where the folder uses an underscore "_".
```clojure
(ns clojure-datavis.core
  (:require [nextjournal.clerk :as clerk]
            [tech.v3.dataset :as ds]))
```
* Start your preferred editor and REPL.  I am using VS Code with the "Calva" extension.
  * Connect your REPL and evaluate the core.clj file.
  

