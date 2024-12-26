#pip install z3-solver

from z3 import *
x = Int('x')
y = Int('y')

def add_equation(line):
    parts = line.split(" ")
    eq = parts[0] * x + parts[1] * y == parts[2]
    solver.add(eq)

total = 0
with open("input.txt") as input_file:
    input_lines = input_file.readlines()
    cursor = 0
    while cursor < len(input_lines):
       solver = Solver()
       add_equation(input_lines[cursor])
       add_equation(input_lines[cursor + 1])
       cursor = cursor + 3
       solver.check()
       if (solver.check() != unsat):
           solution = solver.model()
           print(solution[x], solution[y])
           total = total + 3 * solution[x].as_long() + solution[y].as_long()

print(total)

