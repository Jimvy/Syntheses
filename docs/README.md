To build the documentation, you need to install the following:
- Python 3.5 or more (more is always better, even in terms of pythons)
- `pip3 install sphinx` (to build the doc)
- `pip3 install sphinx_rtd_theme` (to get a more appealing theme)

To build the actual doc, you can use
- On platforms with `make` available: type `make` to get
the list of available targets. The most common one is `make html`.
- On platforms without, typically Windows, type something like
`"C:\Users\Jean-Martin\Documents\Documents\Syntheses\doc\make.bat" html`
to execute `make html`
- For non-Windows platforms without `make`:
either get `make` on your system, or change platform, or search
[the Sphinx documentation](http://www.sphinx-doc.org/en/master/man/sphinx-build.html)
on how to use the `sphinx-build` command. And carry on.
