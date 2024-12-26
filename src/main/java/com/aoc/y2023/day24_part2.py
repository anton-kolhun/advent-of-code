import itertools
import re
import z3
from typing import NamedTuple, List

Hailstone = NamedTuple("Hailstone", px=int, py=int, pz=int, vx=int, vy=int, vz=int, slope=float, intercept=float)

# INPUT PARSING

with open("day24.txt") as input_file:
    input_lines = input_file.readlines()
    input_lines = [line.strip('\n') for line in input_lines]
test_area = (7, 27) if len(input_lines) < 9 else (200000000000000, 400000000000000)
hailstones: List[Hailstone] = []
for line in input_lines:
    px, py, pz, vx, vy, vz = [int(s) for s in re.findall(r'-?\d+', line)]
    # create line equations
    # y = ax + b
    slope = vy / vx
    intercept = py - slope * px
    hailstones.append(Hailstone(px, py, pz, vx, vy, vz, slope, intercept))

pxr, pyr, pzr, vxr, vyr, vzr = z3.Reals("pxr pyr pzr vxr vyr vzr")
solver = z3.Solver()
# only 3 hailstones are needed here;  each creates 1 variable and 3 equations, for a total of 9 vars and 9 eqs
for k, h in enumerate(hailstones[:3]):
    tK = z3.Real(f"t{k}")
    solver.add(tK > 0)
    solver.add(pxr + tK * vxr == h.px + tK * h.vx)
    solver.add(pyr + tK * vyr == h.py + tK * h.vy)
    solver.add(pzr + tK * vzr == h.pz + tK * h.vz)
solver.check()
total = sum(solver.model()[var].as_long() for var in [pxr, pyr, pzr])
print(total)