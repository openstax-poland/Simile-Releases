# Simile-Releases

1. Uruchom Terminal w katalogu, w którym znajduje się Simile.
2. Uruchom polecenie: mvn clean package.
3. Po zakończeniu budowania skopiuj pliki Simile-0.1-jar-with-dependencies.jar oraz Simile-0.1.jar do folderu głównego Simile.
4. (Opcjonalne) Usuń elementy, które mają duży rozmiar, żeby stworzona paczka była lżejsza. Najczęściej należy usunąć:
    1. cnb-book-scanner/node_modules
    2. reports/logs
    3. ScanDB
5. Spakuj wszystkie pliki do ZIPa.
6. Wrzuć stworzony plik ZIP do repozytorium Simile-Releases.
7. Z poziomu zakładki Releases, korzystając z przycisku Draft a new release - przygotuj nowy release.
8. Wszystkie linki z instrukcji prowadzące do najnowszej paczki będą wskazywać na najnowszą wersję.
