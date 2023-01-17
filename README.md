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


