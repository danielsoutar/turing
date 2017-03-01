  ___                 _                           _     _                       
 |_ _|  _ __    ___  | |_   _ __   _   _    ___  | |_  (_)   ___    _ __    ___ 
  | |  | '_ \  / __| | __| | '__| | | | |  / __| | __| | |  / _ \  | '_ \  / __|
  | |  | | | | \__ \ | |_  | |    | |_| | | (__  | |_  | | | (_) | | | | | \__ \
 |___| |_| |_| |___/  \__| |_|     \__,_|  \___|  \__| |_|  \___/  |_| |_| |___/


1) cd [File path to java folder here...]

2) javac *.java

3) java runtm <Turing Machine description file> <Input file> <Optional flag>

e.g: java runtm Turing.txt input.txt




Notes: 

* ’Interactive’ mode prints out the Machine’s traversal of the input, as well as the current transition. This is useful for both debugging the program and testing machine descriptions.

 - To use this mode, append the “-I” flag. 

 - e.g: java runtm Turing.txt input.txt -I

* ’Performance’ mode prints out the number of steps that the Machine makes in order to evaluate the input. For example, suppose the string ‘abcabcbacba’ (which is palindromic) was passed to the palindrome description (‘palindrome.txt’) attached. Then the machine would take 47 steps to traverse the input and accept it. You can confirm this for yourself, if you like.

 - To use this mode, append the “-P” flag.

 - e.g: java runtm palindrome.txt input.txt -P

* You can use both flags together, and in any order.

 - e.g: java runtm palindrome.txt input.txt -I -P

 
#######################################################################################
 
Recognise from the language {w#n | w is any sequence of characters and n is the length of w} over the alphabet {a, b, c}
 
For instance, abcabc#6_, #0_, and abbabba#7_ belong to the language, but abba#5_, aaa3_, and #000_ do not.