 ___                 _                           _     _                       
|_ _|  _ __    ___  | |_   _ __   _   _    ___  | |_  (_)   ___    _ __    ___ 
 | |  | '_ \  / __| | __| | '__| | | | |  / __| | __| | |  / _ \  | '_ \  / __|
 | |  | | | | \__ \ | |_  | |    | |_| | | (__  | |_  | | | (_) | | | | | \__ \
|___| |_| |_| |___/  \__| |_|     \__,_|  \___|  \__| |_|  \___/  |_| |_| |___/
############################################################################################

1) cd [File path to turing folder here...]

2) javac *.java

3) java runtm <Turing Machine description file> <Input file> <Optional flags>

e.g: java runtm Turing.txt input.txt




Notes: 

* ’Interactive’ mode prints out the Machine’s traversal of the input, as well as the current transition. This is useful for both debugging the program and testing machine descriptions.

 - To use this mode, append the “-I” flag. 

 - e.g: java runtm Turing.txt input.txt -I

* ’Performance’ mode prints out the number of steps that the Machine makes in order to evaluate the input. For example, suppose the string ‘abcabcbacba’ (which is palindromic) was passed to the palindrome description (‘palindrome.txt’) attached. Then the machine would take 47 steps to traverse the input and accept it. You can confirm this for yourself, if you like.

 - To use this mode, append the “-P” flag.

 - e.g: java runtm palindrome.txt input.txt -P

* You can use both flags together, and in any order (although they must come at the end).

 - e.g: java runtm palindrome.txt input.txt -I -P