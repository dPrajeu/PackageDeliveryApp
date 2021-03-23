import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dispecerat {
    @SneakyThrows
    public static void main(String[] args) {

        File file = new File("src/main/resources/Package.csv");

        ArrayList<Integer> totalValueArray = new ArrayList<>();
        ArrayList<Integer> totalRevenueArray = new ArrayList<>();

        Files.lines(file.toPath())
                .map(Dispecerat::getPackageFromFile)
                .collect(Collectors.groupingBy(x -> new ArrayList<String>(Arrays.asList(x.getLocation(), x.getDeliveryDate().toString()))))
                .entrySet()
                .stream()
                .forEach(x -> {
                    Runnable runnable = () -> threadAction(x);
                    Thread deliverAPackage = new Thread(runnable);
                    deliverAPackage.start();
                    sleep((x.getValue().stream().mapToInt(y -> y.getDistance())
                            .sum()));
                    System.out.println("Active thread counter pt control: " + Thread.activeCount() + Thread.currentThread());
                    totalValueArray.add(x.getValue().stream().mapToInt(y -> y.getPackageValue())
                            .sum());
                    totalRevenueArray.add((x.getValue().stream().mapToInt(y -> y.getDistance())
                            .sum()));
                });

        System.out.println("Total value of delivered packages is: " + totalValueArray.stream().reduce(0, (a, b) -> a + b) + " Lei");
        System.out.println("Total revenue of delivered packages is: " + totalRevenueArray.stream().reduce(0, (a, b) -> a + b) + " Lei");
    }

    private static void threadAction(Map.Entry<ArrayList<String>, List<Package>> x) {

        x.getValue().stream()
                .forEach(p -> {
                    System.out.println("[Delivering for <" + p.getLocation() + "> and date <" + p.getDeliveryDate() + "> in <" + p.getDistance() + "> seconds]");
                });
    }

    public static Package getPackageFromFile(String line) {
        String[] lines = line.split(",");
        return Package.builder()
                .location(lines[0])
                .distance(Integer.parseInt(lines[1]))
                .packageValue(Integer.parseInt(lines[2]))
                .deliveryDate(LocalDate.parse(lines[3]))
                .build();
    }

    public static void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}