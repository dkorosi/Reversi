# Specifikáció

## Reversi
A reversi egy kétszemélyes táblajáték amelyet 8x8-as táblán játszanak, 64 db
két különböző oldalú koronggal.
Több variánsa is létezik a játéknak, a feladatban a leggyakrabban
alkalmazott szabályrendszert valósítjuk meg (Othello).

### Szabályok
A táblára az egyik játékos *világos*, míg a másik *sötét* korongokat helyez el
felváltva. 

Egy lépés akkor érvényes, ha a soron következő játékos korongja
  - egy üres mezőre kerül
  - egy másik saját koronggal közrefogja az ellenfél korongját/korongjait
    (ezt megteheti vízsintesen, függőlegesen és átlósan is)

Egy érvényes lépés után az összes közrefogott korong megfordul, így színt vált.
Amennyiben a játékosnak nincs lehetősége érvényes lépésre, passzolnia kell,
így ismét az ellenfél lép. Viszont ha van lehetőség érvényes lépésre, akkor
tilos passzolni.

### A játék célja
A cél, hogy a korongok többségének színe a játékosé legyen.

### A játék kezdete
A táblán a játék kezdetén a középső négy rácsra átlósan el kell helyezni 2-2
sötét és világos korongot. A sötét korongok ÉK-DNY, míg a világos korongok 
ÉNY-DK irányúak.

### A játék vége
Ha megtelik a tábla, vagy ha egyik játékos sem tud érvényes lépést tenni,
azaz ha egymás után mindkét játékos passzol. Ilyenkor meg kell számolni a
táblán lévő korongokat szín szerint. A több koronggal rendelkező játékos nyer.

## Az alkalmazás funciói
Az alkalmazás több extra funkcióval is rendelkezik, ezzel növelve a játékélményt.

### Egy- és többjátékos mód
A felhasználó képes a saját számítógépén AI ellen játékot indítani, emellett helyi hálózaton képes egy másik személy ellen játszani. A hálózati játék megkezdése előtt meg kell adni egy felhasználónév és IP cím kombinációt, amely segítségével kapcsolódhat a két kliens.

### Többszintű néhézségi szint
A felhasználó az egyszemélyes játékmódban több nehézségi szinten tud a számítógép ellen játszani. 
Emellett többjátékos módban lehetőség van maximális lépésre fordítható idő beállítására, ezzel is finomhangolva a nehézségi szintet.

### Grafikus Interface
A játék indítása és a beállításai is mind grafikus felületen történnek. A játék alatti grafikus interface segítségével a játékosok követni tudják a játék alakulását, ezen keresztül képesek lépni és a saját, illetve az ellenfél lépéseit, a tábla jelenlegi állását megtekinteni. Ezen felül képesek megtekinteni az érvényes lépéseket.

### Mentés és betöltés
Egyjátékos módban lehetőség van a játékállás elmentésére és későbbi betöltésére.

Többjátékos müdban a végeredmények elmentésére és ezek megjelenítésére.
