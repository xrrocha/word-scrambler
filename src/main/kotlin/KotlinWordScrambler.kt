package wscrambler

import java.io.File

object KotlinWordScrambler {

  // 4 or more latin letters with 2 or more distinct inner letters
  private val WORD_REGEX =
    """\p{IsLatin}(\p{IsLatin})\1*(?!\1)\p{IsLatin}\p{IsLatin}+""".toRegex()

  @JvmStatic
  // Scramble words from files (or stdin) to stdout
  fun main(args: Array<String>) {

    // Collect file readers from args (or stdin if no args)
    val readers =
      if (args.isNotEmpty()) args.map { File(it).reader() }
      else listOf(System.`in`.reader())

    // Swallow all readers into a single string
    val content = readers.joinToString("\n") { it.readText() }

    // Scramble words and print to stdout
    val scrambledContent = scrambleWords(content)
    println(scrambledContent)
  }

  // Scramble words within a given plain text
  fun scrambleWords(inputText: String): String {

    // Copy input text to output array
    val resultChars = inputText.toCharArray()

    // Examine text looking for word matches
    WORD_REGEX.findAll(inputText).forEach { match ->
      // Define range of letters to be shuffled
      val range = match.range.first + 1 until match.range.last
      do {
        // Shuffle inner letter array
        for (i in range) {
          val randomIndex: Int = range.random()
          // Swap current char and random char
          resultChars[randomIndex] = resultChars[i].also {
            resultChars[i] = resultChars[randomIndex]
          }
        }
        // Ensure shuffled array is different from original one!
      } while (range.all { resultChars[it] == inputText[it] })
    }

    // Return scrambled text as string
    return String(resultChars)
  }
}