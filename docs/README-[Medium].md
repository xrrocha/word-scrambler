![](img/tl-dr.png)
This article presents a Java implementation of a "legible word scrambler" and
contrasts it with an equivalent Kotlin implementation that showcases Kotlin's
strengths. The reader is assumed to be proficient with Java.

![](img/cambridge-research.png)

## What's in a Word?

A word is a sequence of letters (duh!). A letter is an alphabetic character
coming in various flavors:

- lowercase
- UPPERCASE
- Àcçëntúãtêd

A _legible scrambled word_ is a modified word where

- The first and last letters are left undisturbed
- The inner letters are shuffled so some or all of them change their positions

We'll be inmepltmneig a leglibe wrod sramecblr tcwie (oh, yaeh!). Frsit in Jvaa,
for the reaedr to conecnt and tehn in Koitln for the radeer to be enlgtnieehd.


### More on Scrambled Words

A legible scrambled word must retain its first and last letters and randomly
shuffle its inner letters at will...

... but restrictions apply:

- It must be at least four letters in length for scrambling to make sense
- Inner letters must contain at least two _distinct_ letters

To ascertain the four-letter rule, consider the following words:

|Word|Comment                                   |
|----|------------------------------------------|
|I   |No inner letters to speak of              |
|We  |No inner letters to speak of              |
|You |Only one inner letter; can't shuffle      |
|They|Four letters; now we can shuffle! (_Tehy_)|

Having four or more letters is not enough, though. Consider now the following
words:

- `Good`
- `Seed`
- `Coool` (_teen spelling_)

Note how, despite their being four letters or more in length, the inner letters
are all the same. Such words would all scramble to themselves and are not to
be processed.

### The Art of Shuffling

Shuffling relies on a randomizer. However, and especially for  short words, a
randomizer may occasionally yield the same, original inner letter ordering.
This can be quite annoying for our goal. Thus, shuffling must be repeated
until a different inner letter order is produced

> Baetnigs wlil cntinoue utinl morael imporevs

For long words, on the other hand, too much shuffling may cause the
scrambled word to become unreadable.

A [string similarity](https://en.wikipedia.org/wiki/String_metric) metric
measures how similar two strings are. Similarity scores oscillate between 0.0
(unrelated) and 1.0 (identical.) Applying the normalized [Levenshtein](https://en.wikipedia.org/wiki/Levenshtein_distance)
similarity metric to the word _unmistakable_ and a few of its scrambled
forms we get:

|Similarity|Legibility|Shufflings              |
|:--------:|:--------:|:----------------------:|
|0.83      |😃        |Unmistabkale Unlmistakabe Unmstakaible|
|0.50      |😐        |Utamislkabne Uniasbamktle Unmbtlaikase|
|0.25      |😔        |Uaknlibsmtae Usialbntamke Usinabmlatke|
|0.16      |😖        |Uaaltmbnksie Utambkilnsae Ualmakbnitse|

An interesting  improvement could be to keep shuffling until we find a
scrambled form that has a Levenshtein similarity of at least 0.75 with its
source word. For the sake of simplicity, we won't implement any such
refinements here.

### Scrambling Words within Free-form Text

Scrambling words within text requires scrambling only words while leaving
all other, non-alphabetic content (whitespace, punctuation) unchanged.

Consider the following quote and its scrambled form:

🄸'🄼 🅃🅆🄾 🅆🄸🅃🄷 🄽🄰🅃🅄🅁🄴 ― 🅆🄾🄾🄳🅈 🄰🄻🄻🄴🄽

🄸'🄼 🅃🅆🄾 🅆🅃🄸🄷 🄽🄰🅃🅁🅄🄴 ― 🅆🄾🄳🄾🅈 🄰🄴🄻🄻🄽

Note that nothing changes length and (except for inner letters, of course)
nothing else changes position either.

We can clone the original text into a character array and operate on this
copy overwriting only the inner letter word regions with their scrambled
incarnations.

All we need is the starting and ending position of each word; neat!

Armed with this knowledge we're ready for our Java implementation.

> _All code below split for readability on small-screen devices_

## First in Java...

Our Java implementation, in all its shining glory, is:

https://gist.github.com/xrrocha/24603c82c969c3268c425df0570be394

To the trained Java eye, the above code should be self-explanatory
(or so we hope 😉)

We offer plenty of clarifying commentary below so read on!

## ...Then in Kotlin

Having shown the Java implementation, the Kotlin one can now be
presented in all its dazzling splendor:

https://gist.github.com/xrrocha/293dd57cec40bc8208ed5253e9ba330a


Again, to the trained Java eye, the above code should be readily
understandable. Anything obscure in either implementation should
hopefully be clarified by the explanations below.

### Kotlin is Familiar

Seasoned Java developers should have little difficulty parsing the above
Kotlin code even if a few constructs don't have obvious Java counterparts.

This is by design: Kotlin was conceived to depart as little as possible from
established Java syntax. This is also true of API's: JVM Kotlin builds upon
familiar Java API's while  retaining compatibility and enriching them in
intuitive ways.

Thus, when we say:

https://gist.github.com/xrrocha/3eb39a29c17466a9fc4f765d7d273c03

we're actually implying:

https://gist.github.com/xrrocha/e41d80eff2a6bfb1e525e8a58e3fd5d5

which is Kotlinese for Java's:

https://gist.github.com/xrrocha/95600da6b2ca1f760e0dcc5b14ec592a

### Kotlin is Compact

Our complete, commented Java implementation is 84 lines while the Kotlin one
is a mere 54.

Unlike some other languages, such brevity is _not_ achieved at the expense of
readability; quite the contrary. It could be argued that, to the casual reader,
the Kotlin version is probably easier to follow than the Java one.

While Java lambdas allow for concise, crisp code (often one-liners),
things can get complicated as shown below.

Compare the following Kotlin code (stolen from our scrambler's `main` method):

https://gist.github.com/xrrocha/e4ae4dc43e89715437645831f084f067

and its Java counterpart:

https://gist.github.com/xrrocha/cac0e9eebd5f868547fcd6546952186a

Here, Kotlin is much more concise (and readable) because:

- Arrays have high-level methods such as `isNotEmpty()`
- `If/Else` is an expression, not a statement. Thus, it can be used in
  assignments
- _Extension functions_ (like `File.reader()` and `reader.readText()`)
  enrich existing classes with handy, macro-like functionality
  (more on this below)
- All exceptions are treated as unchecked
- Common lambda patterns, like collecting results in a `String`, are idiomatic

👉 A note on terminology: Kotlin uniformly calls executable units _functions_,
rather than _methods_. For a function to be deemed a method it must be
contained in an object or a class; it must have a _target_. Otherwise,
functions stand on their own.

### (Far) Fewer Imports!

When presenting the full Java implementation above we omitted imports for
brevity. We didn't have to do this with the Kotlin version: there's only one
import (`import java.io.File`.)

The reason is that while Java's _prelude_ (the set of classes that can be
used without importing) is limited to package `java.lang`, Kotlin's prelude
includes a carefully selected set of additional packages that covers a lot
of the most commonly used classes (I/O, ranges, collections, text, etc.)

In our Java implementation, on the other hand, we require 9 imports that can
be abbreviated to, at most:

https://gist.github.com/xrrocha/cf27aa99c88a1a43c170d596f2fe31ba

### Kotlin Extension Functions

Where in Java we say:

https://gist.github.com/xrrocha/fb36bfa8c568c59808521b2377287ffa

in Kotlin, we say:

https://gist.github.com/036163abef53c487af6d89324cb38d53

This looks as if `String` possessed a `toRegex()` method to convert it to a
regular expression (which, of course, it doesn't.)

This is an instance of _extension function_: a function that can be attributed
to an existing class even if we don't have access to that class's source code
or, as is the case with `String`, even if it's a system, final class!

If we wanted to implement the `toRegex()` extension ourselves we'd say:

https://gist.github.com/xrrocha/f6f35c59337bda4c8473d4d0ce2862e1

Here:

- The name of the target class to be "extended" is prepended to the function name
- Inside the function, `this` refers to the target instance of the "extended"
  class
- The function's return type is specified by the trailing "`: Regex`." In Kotlin
  types are specified _after_ variable names.

## More on Crispness

When a function is a simple one-liner we can define it with an equals sign and
do without the curly braces and the `return` statement. We can also omit the
function's return type if it's patently obvious. The above `String.toRegex()`
function is more idiomatically spelled as:

https://gist.github.com/xrrocha/140f8b8f6629de4ebab120e1312e74a4

Note also that, in Kotlin, when using triple quotes around strings, we don't
need to escape the contents. Thus, while in Java we _have to_ escape:

https://gist.github.com/xrrocha/4b4db51cd1b27a32016581f06a73aa8c

in Kotlin, we don't:

https://gist.github.com/xrrocha/b114c3e1e9ff3023a5eba79a07b41b5c

### But... We're Not Using `\p{InLatin}{4,}`

No, we aren't. When we want to ensure the inner letters contain at
least 2 distinct characters then Dr. Jekyll becomes Mr. Hyde:

https://gist.github.com/xrrocha/6062e533fec2064de2f692356550ad98

Ouch! 😀

This regular expression is comprised of the following five parts:

1. `\p{IsLatin}`: we require the first character to be a Latin letter
2. `(\p{IsLatin})`: we require the second character to be a Latin letter as
   well but this time we enclose it in parentheses. This enables us to
   refer to this second letter (as `\1`) later in the same regular expression
3. `\1*`: we allow for the third character (and any subsequent characters)
   to be repetitions of the second letter. The second letter is referenced
   as `\1` and the `*` quantifier allows it to be repeated zero or more times
4. `(?!\1)\p{IsLatin}`: the nub of our regex! We require a Latin letter
   such that it is _not_ equal to the second letter (`(?!\1)`). This is a
   _back-reference negation_
5. `\p{IsLatin}+`: after the previous, non-equal-to-the-second letter we
   allow for one or more (`+`) trailing letters

Thus, this regular expression:

|Matches...|But not...|Because...             |
|:--------:|:--------:|:---------------------:|
|Kotlin    |C         |Just 1 letter          |
|Gödel     |in        |Just 2 letters         |
|neato     |Zöe       |Just 3 letters         |
|compsci   |cool      |All inner letters equal|


### Kotlin Ranges: Less Looping, More Power

Where, in our `scrambleWords(String)` Java method, we wrote:

https://gist.github.com/xrrocha/a24a180549fa5a5dafa2da66085ec9d4

In Kotlin we write:

https://gist.github.com/xrrocha/8db080ad96c6efec1a57c2fddc9ce91d

In Kotlin, ranges are first-class citizens: we can iterate over them and
also treat them as lambda targets (map, filter, fold, etc.)

Thanks to Kotlin ranges the following Java code:

https://gist.github.com/xrrocha/10b588952988091cf73f85c15dffbd18

becomes, simply:

https://gist.github.com/xrrocha/92ed032f03390258fa0d008f84a2f57f

Interestingly, ranges have their own `random()` extension function that can
be invoked without providing a randomizer. That's why our Kotlin
implementation doesn't have  a `Random` instance while the Java one does.

Note how we exploit the `also` extension function to simplify swapping.
It may look "too idiomatic" to some, but it nicely accommodates some Kotlin
showing off... 😏


### Boolean Lambda Expressions

Our shuffling logic is enclosed in a `do/while` loop in both
implementations:

https://gist.github.com/xrrocha/7df92303eb1b69e0270f6bce873b5c28

The multi-line `while` condition might look a bit unusual to some, but bear
in mind the lambda inside the `while` is actually a boolean _expression_,
not a statement!

Whenever all characters in the inner letter region are the same in the
shuffled array and in the input text we have a false scrambling, and we must
re-shuffle.

The corresponding Kotlin code is arguably simpler and more readable despite
being based, too, on a boolean lambda expression:

https://gist.github.com/xrrocha/321d03ada7e077ae7c87ac9ecda9a202

Here `it` is the implicit name assigned to the lambda parameter when one
is not explicitly declared.

Note how both arrays (`result`) and strings (`text`) are uniformly
indexed with the `[]` operator. This is also true of collections such as `List`
and `Map`!

### The `main` Function

A naked `main` function is executable from the command line. Thus, if we have:

https://gist.github.com/xrrocha/8101a8ec0365387ec6e98fb067cf7c76

We execute it with:

https://gist.github.com/xrrocha/fac96777d4d7f36da0e05e524eadcc7b

`main`'s (synthesized) fully-qualified class name is formed concatenating:

- The package name
- The file name, and
- The "`Kt`" suffix

Note the type `Array<String>`. Kotlin arrays are regular generic types,
just like collections! They're indexed with `[]`, as in Java, and are as
efficient as their Java counterparts because, despite appearances, they
_are_ Java arrays.

https://gist.github.com/xrrocha/fd021176ead9a89535cf48b995bc5b63

If a `main` function requires no arguments they can be omitted, and the function
is still executable:

https://gist.github.com/xrrocha/d17fe07dd050b33ef101ba3de0f8e098

For illustration purposes here is the complete Kolin implementation of the
`main` function for our word scrambler:

https://gist.github.com/xrrocha/de0fafc47845ee303d3453815e98ae9f

### Packages Can Have Members

The astute reader may have noticed that, unlike in Java, in Kotlin our
`scrambleWords()` and `main()` functions are not contained in a class. This
is also true of the `WORD_REGEX` value.

Functions `scrambleWords` and `main`, as well as value `WORD_REGEX`, belong to
their enclosing `wscrambler` _package_ and, if not imported, can be referred
to as:

https://gist.github.com/xrrocha/db18b7fc7380f74435a41de363227a0f

In general, a Kotlin package can directly contain objects, values, variables,
functions, classes, and annotations; the entire zoo!

Executable statements are _not_ allowed inside packages, though. They can
only appear inside functions.

### Kotlin Objects

We've mentioned "objects" twice so far. What on Earth is a Kotlin _object_?

A Kotlin object is a singleton instance that may (or may not) extend a class,
implement interfaces, and have internal members.

In our example (and for the sake of simplicity) the `WORD_REGEX` value and
the `scrambleWords` function belong directly to their enclosing package
`wscrambler`.

This is something a purist may frown upon as value `WORD_REGEX` should be
actually private to function `scrambleWords` (but not wastefully rebuilt on
every invocation!)

One sensible way to organize things would be:

https://gist.github.com/xrrocha/d6af00bab954210e27110f56f04e4dac

Kotlin classes can have associated _companion objects_ holding what in
Java-land we'd call static members. A companion object, however, is much
more than a container for static stuff: it doesn't (have to) extend its
associated class, it may implement interfaces, and it may have its own
members.

https://gist.github.com/xrrocha/97e7ac4f1f1271437a1d3d62d357f509

Kotlin doesn't have the notion of static members as Java does (it
doesn't need them.) It does, however, transparently play nice with Java's
static members and, where they are _required_, annotations can be used to
specify them.

https://gist.github.com/xrrocha/3f876d7cf55fb96623760e24e5b38715

👉 Also: classes and functions are public by default (which reduces verbosity.)
Classes, however, are _closed_ (`final`) by default which discourages
"unintended" inheritance abuse.

## Conclusion

Uff! a rather long ride! You made it this far: kudos! You're on
your way to becoming a fulfilled Kotlin developer.

Kotlin has gained traction in the recent years in the Android world, 
thanks in no small part to
[Google's endorsement of it](https://www.infoworld.com/article/3197337/google-endorses-kotlin-for-android-development.html)
as their preferred Android language. Kotlin has also gained lots of traction
in the JVM backend world with
[Spring openly supporting its use](https://spring.io/blog/2017/01/04/introducing-kotlin-support-in-spring-framework-5-0).

Beyond the JVM, Kotlin also [compiles to native binaries](https://kotlinlang.org/docs/reference/native-overview.html)
(via [LLVM](https://llvm.org/)) as well as [to Javascript](https://kotlinlang.org/docs/reference/js-overview.html).

What's not to love?
