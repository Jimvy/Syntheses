.. _epleval:

Document class *epleval*
==========

This document class is the base class for all evaluation-related classes
(like eplexam_, epltest_ and eplmcq_).

This class should not be used directly:

- It is better to create a subclass of this class, such that
  other authors can use it.
- Some parts of this class rely on some command definitions,
  and can't work without; in a sense, this class is *abstract*.

Main features
----------

- Evaluations don't have a table of content (the skiptoc_ option is set);
- Evaluations have access to the eplcommon_ package.
- Evaluations have access to the eplqa_ package.
- Section titles styling is customized, to put the question number
 (which by default is the section number) in a shaded rectangle.

Command definitions
----------

- ``\isepleval``: indicates that the document is an evaluation.
- ``\QAlabel``: sets the label used by the eplqa_ package.
  Default is "Question" in English, and "la Question" in French.
- ``\theQA``: sets the section numbering scheme used by the eplqa_ package.
  Default is ``\thesection``

Subclassing the ``epleval`` document class
----------

In order to create a new document class based on this one,
you need to define the commands of eplbase_.
No additional commands need to be defined.
