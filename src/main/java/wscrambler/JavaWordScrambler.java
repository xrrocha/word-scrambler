package wscrambler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class JavaWordScrambler {

  // 4 or more latin letters with 2 or more distinct inner letters
  private static final Pattern WORD_REGEX = Pattern.compile(
    "\\p{IsLatin}(\\p{IsLatin})\\1*(?!\\1)\\p{IsLatin}\\p{IsLatin}+");

  // Scramble words within a given plain text
  public static String scrambleWords(String inputText) {

    // Copy input text to output array
    final var resultChars = inputText.toCharArray();

    // Create randomizer for this run
    final var random = new Random();

    // Examine text looking for word matches
    WORD_REGEX.matcher(inputText).results().forEach(match -> {
      // Define range of *inner*  letters to be shuffled
      final var start = match.start() + 1; // second
      final var end = match.end() - 1; // penultimate
      final var length = end - start;

      do {
        // Shuffle inner letter array
        for (var i = start; i < end; i++) {
          // Choose a random index in region
          final var randomIndex = start + random.nextInt(length);
          // Swap current char and random char
          final var save = resultChars[randomIndex];
          resultChars[randomIndex] = resultChars[i];
          resultChars[i] = save;
        }
        // Ensure shuffled array is different from original one!
      } while (IntStream.range(start, end).allMatch(i ->
        resultChars[i] == inputText.charAt(i)
      ));
    });

    // Return scrambled text as string
    return new String(resultChars);
  }

  // Scramble words from files (or stdin) to stdout
  public static void main(String[] args) {

    // Collect file readers from args (or stdin if no args)
    final Stream<BufferedReader> readers;
    if (args.length > 0) {
      readers = Arrays.stream(args).map(filename -> {
        try {
          return new BufferedReader(new FileReader(filename));
        } catch (Exception e) {
          System.err.println("Error: " + e.getMessage());
          System.exit(1);
          throw new RuntimeException(e); // appease compiler
        }
      });
    } else {
      readers = Stream.of(
        new BufferedReader(new InputStreamReader(System.in)));
    }

    // Swallow all readers into a single string
    final var content = readers
      .flatMap(BufferedReader::lines)
      .collect(Collectors.joining("\n"));

    // Scramble words and print to stdout
    final var scrambledContent = scrambleWords(content);
    System.out.println(scrambledContent);
  }
}