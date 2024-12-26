package com.aoc.y2023;

import com.aoc.y2023.helper.FilesUtils;
import com.aoc.y2023.helper.ParseUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day20 {

    public static void main(String[] args) {
        task1();
    }

    public static void task1() {
        List<String> lines = FilesUtils.readFile("aoc_2023/day20.txt");
        Map<String, Module> modules = new HashMap<>();
        Set<String> conjs = new HashSet<>();
        List<String> broadCasters = new ArrayList<>();
        for (String line : lines) {
            Module op = new Module();
            List<String> parts = ParseUtils.splitByDelimiter(line, " -> ");
            List<String> dests = ParseUtils.splitByDelimiter(parts.get(1), ", ");
            op.destinations = dests;
            if (parts.get(0).equals("broadcaster")) {
//                for (String dest : dests) {
//                    pulses.add(new PulseInfo(Pulse.LOW, dest));
//                }
                broadCasters.addAll(dests);
            } else {
                op.type = parts.get(0).substring(0, 1);
                op.source = parts.get(0).substring(1);
                modules.put(op.source, op);
                if (op.type.equals("&")) {
                    conjs.add(op.source);
                }
            }
        }
        Map<String, FlipflopState> flipStates = new HashMap<>();
        Map<String, Map<String, Pulse>> conjDeps = new HashMap<>();
        for (Map.Entry<String, Module> entry : modules.entrySet()) {
            for (String destination : entry.getValue().destinations) {
                if (conjs.contains(destination)) {
                    conjDeps.merge(destination, new HashMap(Map.of(entry.getKey(), Pulse.LOW)), (map1, map2) -> {
                        map1.put(entry.getKey(), Pulse.LOW);
                        return map1;
                    });
                }
            }
            if (entry.getValue().type.equals("%")) {
                flipStates.put(entry.getKey(), FlipflopState.OFF);

            }
        }

        BigInteger lowPulsesCounter = BigInteger.ZERO;
        BigInteger highPulsesCounter = BigInteger.ZERO;
        for (int cycle = 0; cycle < 1000; cycle++) {
            List<PulseInfo> pulses = new ArrayList<>();
            lowPulsesCounter = lowPulsesCounter.add(BigInteger.ONE);
            for (String broadCaster : broadCasters) {
                pulses.add(new PulseInfo(Pulse.LOW, broadCaster, "broadCaster"));
                lowPulsesCounter = lowPulsesCounter.add(BigInteger.ONE);
            }
            while (!pulses.isEmpty()) {
                PulseInfo pulseInfo = pulses.remove(0);
                var module = modules.get(pulseInfo.sentTo);
                if (module != null) {
                    if (module.type.equals("%")) {
                        FlipflopState state = flipStates.getOrDefault(module.source, FlipflopState.OFF);
                        if (pulseInfo.pulse == Pulse.LOW) {
                            state = state.flip();
                            flipStates.put(module.source, state);
                            var pulseToSend = Pulse.LOW;
                            if (state == FlipflopState.ON) {
                                pulseToSend = Pulse.HIGH;
                            }
                            for (String destination : module.destinations) {
                                pulses.add(new PulseInfo(pulseToSend, destination, module.source));
                                print(pulseInfo.sentTo, new PulseInfo(pulseToSend, destination, module.source));
                                if (pulseToSend == Pulse.HIGH) {
                                    highPulsesCounter = highPulsesCounter.add(BigInteger.ONE);
                                } else {
                                    lowPulsesCounter = lowPulsesCounter.add(BigInteger.ONE);
                                }
                            }
                        }
                    } else if (module.type.equals("&")) {
                        conjDeps.get(module.source).put(pulseInfo.sentFrom, pulseInfo.pulse);
                        for (String destination : module.destinations) {
                            var deps = conjDeps.get(module.source);
                            boolean allPulsesHigh = true;
                            for (Map.Entry<String, Pulse> dep : deps.entrySet()) {
                                if (dep.getValue() == Pulse.LOW) {
                                    allPulsesHigh = false;
                                    break;
                                }
                            }
                            var pulseToSend = Pulse.HIGH;
                            if (allPulsesHigh) {
                                pulseToSend = Pulse.LOW;
                            }
                            if (pulseToSend == Pulse.HIGH) {
                                highPulsesCounter = highPulsesCounter.add(BigInteger.ONE);
                            } else {
                                lowPulsesCounter = lowPulsesCounter.add(BigInteger.ONE);
                            }
                            pulses.add(new PulseInfo(pulseToSend, destination, module.source));
                            print(pulseInfo.sentTo, new PulseInfo(pulseToSend, destination, module.source));
                        }
                    }
                }
            }
            System.out.println();
        }
        System.out.println(lowPulsesCounter.multiply(highPulsesCounter));
    }

    private static void print(String source, PulseInfo pulseInfo) {
        System.out.println(String.format("%s -%s-> %s", source, pulseInfo.pulse.toString().toLowerCase(), pulseInfo.sentTo));
    }


    @AllArgsConstructor
    @NoArgsConstructor
    private static class Module {
        String type;
        String source;
        List<String> destinations = new ArrayList<>();
    }

    private static enum Pulse {
        LOW, HIGH;

        Pulse invert() {
            if (this == LOW) {
                return HIGH;
            }
            return LOW;
        }
    }

    private static enum FlipflopState {
        ON("on"), OFF("off");

        private String value;

        FlipflopState(String value) {
            this.value = value;
        }

        FlipflopState flip() {
            if (this == ON) {
                return OFF;
            }
            return ON;
        }

        public static FlipflopState ofValue(String val) {
            for (FlipflopState value : values()) {
                if (value.value.equals(val)) {
                    return value;
                }
            }
            return null;
        }

    }

    @AllArgsConstructor
    private static class PulseInfo {
        private Pulse pulse;
        private String sentTo;
        private String sentFrom;
    }


}
