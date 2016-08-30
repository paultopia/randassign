# randassign

Put it on your path. From releases. 

```
randassign FILE STUDENTS-IDENTIFIER ASSIGNMENTS-IDENTIFIER NUM-ASSIGNMENTS-PER-STUDENT
```

file can be csv or json. 

students-identifier can be either object label (json) or row header (csv) for a list of students

assignments-identifier like students, but with a list of assignments. 

num-assignments-per-student is an integer, representing the number of assignments to be randomly assigned to each student. 

spits out (to stdout) a json of the form [{"student1": ["assignment1", "assignment2", "...assignmentn"]}, {"student2": [...]}] following the following rules: 

1.  No student is given the same assignment twice.
2.  The list of assignments is exhausted before and then recycled to guarantee an even-as-possible distribution of students to assignments. 

If you want to save that json you should probably pipe it to a file.

MIT License.  
