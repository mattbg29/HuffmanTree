Name:  Matthew Green

Date: 2Nov2022

IDE:  I am using IntelliJ IDEA Community Edition 2021.1.2 for this lab

Java version:  I am using Java Version 8 Update 281

Program overview:
Run this program through the Main class.  Users will be directed to input the
source names for two or more of the following files: a table with symbols and frequencies,
a list of phrases to encode, a list of coded phrases to decode.  If the user inputs phrases
to encode, the user can opt to either have every phrase in the file encoded using one external
frequency table, or to have each phrase encoded using a custom frequency table derived from
the phrase itself.  If the latter option is chosen, each phrase will generate a separate output
file with the custom information relating to the resulting frequency table.

Decoding phrases:
When the user inputs a file with more than one strings to decode, each string will be decoded
by a single frequency table that the user provides.

Frequency table format:
If a frequency table is provided, it must have the following format:
Symbol - Frequency, ie:
U - 15
Any other format will result in an error message and the user wil be prompted to try again.
Any symbols that repeat will be ignored, though a console message will inform the user that
such occurred.

Frequency table capitalization/punctuation:
When a frequency table is included, only the symbols provided will be used for encoding,
though the encoder will ignore capitalization unless both capital and lower case letters
are in the frequency table.  For instance, if the frequency table includes only upper-case
letters, but the phrase has upper and lower case letters, the encoder will capitalize all
letters as it encodes.  If however the frequency table has upper and lower case letters,
the encoder will preserve capitalization.  Any symbol completely excluded from the frequency
table (punctuation, or a letter that was omitted) will be skipped during the encoding process.

Custom frequency tables when phrases are input:
When a user inputs a phrase and requests a custom frequency table, the table will include
every symbol that appears in the phrase and omit all else.

Text files:
In addition to this README, you will find the following text files included with this lab:
coded_phrases: a list of phrases encoded with frequency_table
coded_phrases_output: the coded_phrases from above decoded
frequency_table: the aforementioned frequency table, as supplied in the lab
frequency_table_output: the output produced by creating a huffman tree with the given frequency_table
phrases: a series of phrases to be encoded (whose encodings, with the frequency_table, is in the coded_phrases file)
phrases_output: the result of encoding phrases with the frequency_table
coded_romeo: an excerpt from Romeo and Juliet encoded with its own frequency table
frequency_table_romeo: the custom frequency table used to encode romeo, derived from its own text
romeo_output: coded_romeo decoded using frequency_table_romeo



