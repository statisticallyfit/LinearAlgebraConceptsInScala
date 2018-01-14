package org.specs2.common

import org.specs2.execute.Snippet
import org.specs2.io.{FileSystem, FilePath}

import scala.reflect.ClassTag

object SnippetHelper {

  def load(path: FilePath): Snippet[Unit] =
    Snippet(
      code = () => (),
      codeExpression = FileSystem.readFile(path).runOption.orElse(Option("no file found at "+path.path))
    )

  def incl[T : ClassTag]: Snippet[Unit] = {
    val name = implicitly[ClassTag[T]].runtimeClass.getName
    load(FilePath.unsafe("main/src/test/scala/"+name.replace(".", "/")+".scala")) //@todo avoid hardcoding the module name
  }

}