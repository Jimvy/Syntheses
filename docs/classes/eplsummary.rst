.. _eplsummary:

Document class *eplsummary*
==========

this document class is used for summaries:
relatively short documents that contain all the informations required
to succeed well at the exam of the corresponding course,
that will be used to quickly review the material just before the exam,
and that don't pretend to replace the magistral courses, syllabi
or reference books for the understanding of the course material.

Examples of content that can find its way in a summary:

- the general structure of the course, in the form of corresponding titles
  and schematics;
- important definitions, theorems, methods, schematics and short reflexions
  used in the course. The level of importance depends on the course and is
  at the author's discretion;
- list of definitions, if appropriate (think Oz);

Examples of content that **should not** find its way in a summary:

- exams or exercises resolution. Please have a look at their corresponding
  classes, :ref:`eplexam` and eplexercises_. Note that you can put
  *general* methods used for solving the exercises in a summary,
  accompanied by a short exercise if required;
- detailed explanations of the course content and material, magistral courses
  content, proofs of the theorem: you should put them in an eplnotes_ document.
  However, if the reasoning behind the proofs or other explanations is likely
  to be used again at the exam (either because the same proof has to be given
  or similar theorems with similar proofs will appear), and if there are
  relatively few of them, you can put them.
- excerpts from the syllabi or the reference books: most of the time, they're
  under copyrights from the author (and you will get in troubles),
  and most students are expected to own or have access to a copy of the syllabi
  or reference books.

Remember: a summary should serve as a reference document for last-minute check
and reviews. A summary with a size that is of the same order of magnitude
as the course documents is useless.

Main features
----------

This class builds directly upon the eplbase_ class, and doesn't add
any significant feature. Refer to the eplbase_ class for further informations.

Commands definitions
----------

``\hypertitle[<french apostrophe>]{<title>}{<quadri>}{<dept code>}{<in-dept code>}{<authors>}{<professors>}``
''''''''''

This command defines the title and content of the preface page.
The parameters have the same meaning as the parameters of ``\basehypertitle``,
