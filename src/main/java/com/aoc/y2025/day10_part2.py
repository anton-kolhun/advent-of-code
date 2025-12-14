#pip install z3-solver

from z3 import *
import re


def parse_machine(line):
    buttons = []
    for match in re.finditer(r'\(([0-9,]+)\)', line):
        button = [int(x) for x in match.group(1).split(',')]
        buttons.append(button)
    joltage_match = re.search(r'\{([0-9,]+)\}', line)
    targets = [int(x) for x in joltage_match.group(1).split(',')]
    return buttons, targets


def solve_machine(buttons, targets):
    num_buttons = len(buttons)
    num_counters = len(targets)

    opt = Optimize()

    presses = [Int(f'button_{i}') for i in range(num_buttons)]

    for p in presses:
        opt.add(p >= 0)

    for counter_idx in range(num_counters):
        counter_sum = 0
        for button_idx, button in enumerate(buttons):
            if counter_idx in button:
                counter_sum += presses[button_idx]

        opt.add(counter_sum == targets[counter_idx])

    total_presses = Sum([p for p in presses])
    opt.minimize(total_presses)

    if opt.check() == sat:
        model = opt.model()
        total = sum(model[p].as_long() for p in presses)
        return total
    else:
        return None


def solve_all_machines(input_text):
    """Solve all machines and return total minimum button presses."""
    lines = input_text.strip().split('\n')
    total_presses = 0

    for i, line in enumerate(lines, 1):
        buttons, targets = parse_machine(line)
        min_presses = solve_machine(buttons, targets)

        if min_presses is not None:
            print(f"Machine {i}: {min_presses} presses")
            total_presses += min_presses
        else:
            print(f"Machine {i}: No solution found")
            return None

    return total_presses


if __name__ == '__main__':
    with open('input.txt') as f:
        puzzle_input = f.read()
        result = solve_all_machines(puzzle_input)
        print(f'Answer: {result}')



