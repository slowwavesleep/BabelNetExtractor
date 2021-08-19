# BabelNetExtractor
Scala tool to extract data from local BabelNet indices

Currently, the tool does the following:
- reads nouns from `nouns_no_header.csv`
- queries local BabelNet with these nouns (the expected results are also nouns in English)
- stores BabelNet ID, candidate FrameNet ID, BabelNet definition, candidate FrameNet definition, and all the edges for each synset in `bn_entries.csv` and `bn_edges.csv`

And that's basically it so far.

## Steps to reproduce

- Clone the repository
```
git clone https://github.com/slowwavesleep/BabelNetExtractor.git
```
- Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) (Community version will suffice) if you don't have it
- [Install Scala plugin](https://www.jetbrains.com/help/idea/discover-intellij-idea-for-scala.html)
- Open the cloned repository with IDEA (File -> Open)
- Add BabelNet API to the libraries:

  - File -> Project Structure -> `+` -> Java


  ![image](https://user-images.githubusercontent.com/44175589/130072884-16192a3f-2a4c-485b-b8be-fa20c8b09944.png)
  
  
  - Navigate to BabelNet API and select the corresponding `.jar` file (such as `/BabelNet-API-3.6.1/babelnet-api-3.6.1.jar`)
  - Add `lib` directory the same way (such as `/BabelNet-API-3.6.1/lib`)
  - The list of libraries should look like this:


  ![image](https://user-images.githubusercontent.com/44175589/130075221-c7fbfb31-1efc-4d29-97f6-604093aaf34b.png)



- Copy `config` directory from BabelNet API to the project
- Point to local indices in `config/babelnet.var.properties` (for example, `babelnet.dir=/home/user/BabelNet/BabelNet-3.6`) and
comment out the rest of parameters in this files unless you know what you're doing 
- Run `src/main/scala/Extract.scala` using IDEA (right click on it, then `Run 'Extract'`)
- After a few minutes `bn_entries.csv` and `bn_edges.csv` should appear in `src/main/resources`
