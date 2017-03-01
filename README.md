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
 
Recognise from the language {#n#w | w is any sequence of characters and n is the length of w} over the alphabet {a, b, c}
 
For instance, #6#abcabc_, #0#_, and #7#abbabba_ belong to the language, but #abba#5_, #3aaa_, and #000#_ do not.


4#abba_

0) can check for how many 0s as you start X
    - if 0, then if next character is not a hash and character after that _, reject (no trailing 0s) X
    - if not, carry on X
1) go to second hash, step back one (will be at smallest digit) X
2) if not 0:
    - decrement by one, mark off a character in input, rewind to second hash X
   if 0:
    - move left until non-0 number or hash, decrement by 1 if number, replace with 9s as you move right, mark off character in input, rewind to second hash
    - if no non-0 character by the time you reach a hash, move to end of input. If any non-X letters after second hash, reject. Otherwise reach _ and accept.
   