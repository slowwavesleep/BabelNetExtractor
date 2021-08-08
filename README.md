# BabelNetExtractor
Scala tool to extract data from local BabelNet indices

Currently, the tool does the following:
- reads nouns from `nouns_no_header.csv`
- queries local BabelNet with these nouns (the expected results are also nouns in English)
- stores BabelNet ID, candidate FrameNet ID, BabelNet definition, candidate FrameNet definition, and all the edges for each synset in `bn_entries.csv` and `bn_edges.csv`

And that's basically it so far.
