<div align="center">

<!-- ANIMATED BANNER -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=6366f1,a855f7&height=200&section=header&text=Telephone%20Directory&fontSize=42&fontColor=ffffff&fontAlignY=38&desc=Java%20Swing%20GUI%20Application&descAlignY=58&descSize=18&animation=fadeIn" width="100%" />

<!-- TYPING ANIMATION -->
<a href="https://git.io/typing-svg">
  <img src="https://readme-typing-svg.demolab.com?font=Segoe+UI&weight=700&size=22&pause=1000&color=6366F1&center=true&vCenter=true&width=600&lines=📞+Full-Featured+Contact+Manager;⚡+Merge+Sort+%2B+Binary+Search;↩️+Undo+%2F+Redo+Stack;🎵+Sound+Effects+%2B+Dark+UI;📂+CSV+Import+%2F+Export" alt="Typing SVG" />
</a>

<br/>

<!-- BADGES ROW 1 -->
[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![Swing](https://img.shields.io/badge/Java%20Swing-GUI-6366f1?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Platform](https://img.shields.io/badge/Platform-Windows%20%7C%20macOS%20%7C%20Linux-14b8a6?style=for-the-badge)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI)
[![License](https://img.shields.io/badge/License-MIT-a855f7?style=for-the-badge)](LICENSE)

<!-- BADGES ROW 2 -->
[![Stars](https://img.shields.io/github/stars/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI?style=for-the-badge&color=f59e0b&logo=github)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI/stargazers)
[![Forks](https://img.shields.io/github/forks/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI?style=for-the-badge&color=10b981&logo=github)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI/forks)
[![Issues](https://img.shields.io/github/issues/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI?style=for-the-badge&color=ef4444&logo=github)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI/issues)
[![Last Commit](https://img.shields.io/github/last-commit/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI?style=for-the-badge&color=6366f1&logo=git)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI/commits)

<br/>

> **A modern, feature-rich Telephone Directory desktop app built with Java Swing.**  
> Manage your contacts efficiently with sorting algorithms, undo/redo history, sound effects, CSV import/export — all wrapped in a sleek dark-themed GUI.

<br/>

</div>

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🖥️ Application Preview](#️-application-preview)
- [🎨 UI Color Palette](#-ui-color-palette)
- [🔬 Algorithms & Data Structures](#-algorithms--data-structures)
- [📁 Project Structure](#-project-structure)
- [⚙️ Tech Stack](#️-tech-stack)
- [🚀 Getting Started](#-getting-started)
- [📖 How to Use](#-how-to-use)
- [🌍 Supported Country Codes](#-supported-country-codes)
- [🤝 Contributing](#-contributing)
- [📄 License](#-license)
- [👤 Author](#-author)

---

## ✨ Features

<div align="center">

| 🟣 Core | 🟢 Smart | 🔵 Utility |
|:---:|:---:|:---:|
| ➕ Add Contacts | 🔀 Merge Sort | 📥 Import CSV |
| ✏️ Edit Contacts | 🔍 Binary Search | 📤 Export CSV |
| 🗑️ Delete Contacts | ↩️ Undo / Redo | 🎵 Sound Effects |
| ☑️ Bulk Delete | 📋 Duplicate Detection | 💾 Auto-Save |
| 📞 Country Code Picker | ✅ Phone Validation | 🌙 Dark Theme |

</div>

<br/>

### 🟣 Contact Management
- **Add** contacts with Name, Phone, Email, and Address
- **Edit** any selected contact from the table
- **Delete** single or multiple contacts at once using checkboxes
- **Duplicate detection** for both name and phone number

### ⚡ Smart Algorithms
- **Merge Sort** — sorts all contacts alphabetically by name with `O(n log n)` efficiency
- **Binary Search** — lightning-fast contact lookup by name
- **Undo / Redo** — full action history using a Stack-based system (ADD, UPDATE, DELETE, BULK\_DELETE)

### 🎨 Beautiful UI
- Dark navy/indigo theme with smooth gradient header
- Rounded buttons with hover and press animations
- Custom striped table with row hover highlighting
- Sleek custom scrollbar, rounded input fields, placeholder text

### 📦 Data & File Management
- **CSV Import** — load contacts from any CSV file
- **CSV Export** — save all contacts to a portable CSV
- **Persistent storage** — contacts auto-save to `contacts.csv`

### 🔊 Sound Effects
| Action | Sound File |
|--------|-----------|
| ➕ Add Contact | `Add sound.wav` |
| 🗑️ Delete Contact | `fahhhhh.wav` |
| 🖱️ Button Click | `soft click tap.wav` |

---

## 🖥️ Application Preview

<div align="center">

```
╔══════════════════════════════════════════════════════════════════╗
║  🔷  Telephone Directory         ║  [ 3 contacts ]             ║
║  Merge Sort · Binary Search · Undo/Redo Stack                  ║
╠══════════════════════════════════════════════════════════════════╣
║  CONTACT DETAILS                                                ║
║  Name [________________]  Phone [+91][_______________]          ║
║  Email [___________________________]  Address [______________]  ║
║  [ + Add ]  [ Update ]  [ Delete Selected ]                     ║
╠══════════════════════════════════════════════════════════════════╣
║  SEARCH & TOOLS                                                 ║
║  [Search by name...]  [Search] [Merge Sort] [Undo] [Redo]      ║
║                       [Import CSV]  [Export CSV]               ║
╠══════════════════════════════════════════════════════════════════╣
║  ☐  │  Name        │  Phone        │  Email         │ Address  ║
║─────┼──────────────┼───────────────┼────────────────┼─────────║
║  ☐  │  Alice       │  +1 4155551234│  alice@mail.com │  NY     ║
║  ☐  │  Bob         │  +91 9876543  │  bob@mail.com  │  Delhi  ║
║  ☐  │  Charlie     │  +44 7777888  │  char@mail.com │  London ║
╠══════════════════════════════════════════════════════════════════╣
║  ● Ready                    Click a row to edit · Tick to delete ║
╚══════════════════════════════════════════════════════════════════╝
```

</div>

> **Note:** Add a real screenshot by taking one of the running app and uploading it as `screenshot.png` — then replace this block with:  
> `![App Screenshot](screenshot.png)`

---

## 🎨 UI Color Palette

<div align="center">

| Swatch | Name | Hex | Usage |
|:------:|------|-----|-------|
| 🟦 | **Primary** | `#0D1426` | App background |
| 🟣 | **Accent** | `#6366F1` | Buttons, header, borders |
| 🟢 | **Success** | `#10B981` | Add button, status text |
| 🔴 | **Danger** | `#EF4444` | Delete button |
| 🟡 | **Warning** | `#F59E0B` | Search button |
| 🩵 | **Teal** | `#14B8A6` | Import CSV |
| 🟪 | **Purple** | `#A855F7` | Export CSV, header gradient |
| ⬛ | **Surface** | `#1A2438` | Card panels, table |
| 🔘 | **Input BG** | `#0F172A` | Text field backgrounds |

</div>

---

## 🔬 Algorithms & Data Structures

### 🔀 Merge Sort — `O(n log n)`

Used to sort contacts alphabetically by name. Consistently efficient regardless of input size.

```java
// Divide and Conquer — sorts the directory list in-place
mergeSort(directory, 0, directory.size() - 1);
```

```
Input:  [Charlie, Alice, Bob]
Step 1: [Charlie] [Alice] [Bob]
Step 2: [Alice, Charlie] [Bob]
Output: [Alice, Bob, Charlie] ✅
```

### 🔍 Binary Search — `O(log n)`

Searches through the **sorted** contact list to find a contact by name in logarithmic time.

```java
// Requires list to be sorted first (use Merge Sort button)
int found = binarySearch(directory, searchQuery);
```

```
Sorted: [Alice, Bob, Charlie, Dave, Eve]
Search: "Dave"
 → Mid = Charlie → go right
 → Mid = Dave ✅ Found in 2 steps!
```

### 📚 Undo / Redo Stack — `O(1)` push/pop

Every mutating action (add, update, delete, bulk-delete) creates an `UndoAction` object and pushes it onto the undo stack. Undoing moves the action to the redo stack.

```java
Stack<UndoAction> undoStack = new Stack<>();
Stack<UndoAction> redoStack = new Stack<>();

// Action types tracked:
// ✅ ADD → undo removes the contact
// ✅ DELETE → undo re-inserts the contact
// ✅ UPDATE → undo reverts to previous values
// ✅ BULK_DELETE → undo restores all removed contacts
```

```
User adds Alice    → undoStack: [ADD(Alice)]
User adds Bob      → undoStack: [ADD(Alice), ADD(Bob)]
User presses Undo  → Bob removed; redoStack: [ADD(Bob)]
User presses Redo  → Bob re-added; undoStack: [ADD(Alice), ADD(Bob)]
```

---

## 📁 Project Structure

```
📂 Telephone-Directory-App-Using-Java-Swing-GUI/
│
├── 📜 Caller.java              ← Main application file (1532 lines)
│
├── 🎵 Add sound.wav            ← Played when a contact is added
├── 🎵 fahhhhh.wav              ← Played when a contact is deleted
├── 🎵 soft click tap.wav       ← Played on button clicks
│
├── 🖼️ phone_icon.jpg           ← Application window icon
│
├── 📊 contacts.csv             ← Auto-saved contact data
├── 📊 contacts_export.csv      ← User-exported contact data
├── 📊 new export 1.csv         ← Sample exported data
│
├── ⚙️ run.bat                  ← Windows one-click launcher
│
├── 📂 bin/                     ← Compiled .class files
│   └── Caller.class
│
└── 📄 README.md
```

### 🏗️ Internal Class Architecture

```
Caller (JFrame)
│
├── 📦 Contact                  ← Data model (name, phone, email, address)
├── 📦 UndoAction               ← Tracks ADD / DELETE / UPDATE / BULK_DELETE
│
├── 🎨 GradientPanel            ← Paints gradient backgrounds
├── 🎨 CardPanel                ← Rounded card with optional drop shadow
├── 🎨 RoundedButton            ← Custom button with hover/press states
├── 🎨 RoundedFieldBorder       ← Rounded border for text inputs
├── 🎨 PlaceholderField         ← Text field with hint text
│
├── 📋 StripedRenderer          ← Alternating row colors + hover highlight
├── 👂 RowInteractionListener   ← Handles table row click + hover
├── 🎚️ SleekScrollBarUI         ← Custom minimal scrollbar
└── 🌍 CountryCodeRenderer      ← Formats the country code dropdown
```

---

## ⚙️ Tech Stack

<div align="center">

| Technology | Purpose | Version |
|-----------|---------|---------|
| ☕ **Java SE** | Core language | 8+ |
| 🪟 **Java Swing** | GUI framework (`JFrame`, `JTable`, `JButton`) | Built-in |
| 🎨 **Java AWT** | 2D graphics, gradients, rounded shapes | Built-in |
| 🔊 **javax.sound.sampled** | WAV audio playback | Built-in |
| 📂 **java.io** | CSV file read/write, persistence | Built-in |
| 🗂️ **java.util** | `ArrayList`, `Stack`, `List` | Built-in |

</div>

---

## 🚀 Getting Started

### ✅ Prerequisites

Make sure you have Java installed:

```bash
java -version
# Should output: java version "1.8.0" or higher
```

> Download Java from [https://adoptium.net](https://adoptium.net) if needed.

---

### 📥 Installation

**1. Clone the repository**

```bash
git clone https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI.git
cd Telephone-Directory-App-Using-Java-Swing-GUI
```

**2. Compile the Java source**

```bash
javac -d bin Caller.java
```

**3. Run the application**

```bash
java -cp bin Caller
```

**Or on Windows — double-click `run.bat`** 🪟

```bat
@echo off
javac Caller.java
java Caller
pause
```

---

## 📖 How to Use

### ➕ Adding a Contact

1. Fill in the **Name** field (required)
2. Select your **Country Code** from the dropdown (40+ countries)
3. Enter the **Phone Number** — must be at least **10 digits**
4. Optionally add **Email** and **Address**
5. Click **`+ Add`** — the contact is saved automatically!

> ⚠️ Duplicate phone numbers are **not allowed**. You'll be warned about duplicate names but can proceed.

---

### ✏️ Editing a Contact

1. **Click any row** in the table to load the contact into the form
2. Modify the fields as needed
3. Click **`Update`** to save changes

---

### 🗑️ Deleting Contacts

**Single:** Select a row → click **`Delete Selected`**  
**Bulk:** Tick the **☑️ checkboxes** on the left of multiple rows → click **`Delete Selected`**

---

### 🔍 Searching

1. Type a name (or partial name) into the **Search** box
2. Click **`Search`** — uses **Binary Search** on the sorted list
3. The matching contact is highlighted in the table

> 💡 Tip: Click **`Merge Sort`** first to sort contacts before searching!

---

### ↩️ Undo / Redo

| Button | Action |
|--------|--------|
| `Undo` | Reverses the last Add, Update, or Delete |
| `Redo` | Re-applies the last undone action |

Every action is tracked — you can undo your entire session history!

---

### 📂 Import / Export CSV

**Import:**  
Click **`Import CSV`** → select a `.csv` file with columns: `Name, Phone, Email, Address`

**Export:**  
Click **`Export CSV`** → choose where to save → all contacts are written to the file

**CSV Format:**

```csv
Name,Phone,Email,Address
Alice Smith,+1 4155551234,alice@example.com,San Francisco CA
Bob Jones,+91 9876543210,bob@example.com,Mumbai India
```

---

## 🌍 Supported Country Codes

The app includes **40+ country codes** built-in:

<div align="center">

| 🇮🇳 `+91` India | 🇺🇸 `+1` USA | 🇬🇧 `+44` UK | 🇦🇺 `+61` Australia |
|:---:|:---:|:---:|:---:|
| 🇯🇵 `+81` Japan | 🇨🇳 `+86` China | 🇩🇪 `+49` Germany | 🇫🇷 `+33` France |
| 🇧🇷 `+55` Brazil | 🇰🇷 `+82` South Korea | 🇸🇬 `+65` Singapore | 🇦🇪 `+971` UAE |
| 🇲🇽 `+52` Mexico | 🇿🇦 `+27` South Africa | 🇳🇬 `+234` Nigeria | 🇯🇵 `+81` Japan |

</div>

*...and 24 more countries in the dropdown!*

---

## 🤝 Contributing

Contributions are what make the open source community amazing! 🙌

```bash
# 1. Fork the project
# 2. Create your feature branch
git checkout -b feature/AmazingFeature

# 3. Commit your changes
git commit -m 'Add some AmazingFeature'

# 4. Push to the branch
git push origin feature/AmazingFeature

# 5. Open a Pull Request 🚀
```

### 💡 Ideas for Contribution

- [ ] Add a profile photo per contact
- [ ] Search by phone number or email
- [ ] Dark/Light mode toggle
- [ ] Contact groups / categories
- [ ] Birthday reminders
- [ ] Print contacts to PDF
- [ ] Database backend (SQLite / MySQL)
- [ ] Contact import from Google Contacts JSON

---

## 📄 License

Distributed under the **MIT License**.  
See [`LICENSE`](LICENSE) for more information.

```
MIT License — free to use, modify, and distribute with attribution.
```

---

## 👤 Author

<div align="center">

**jschouhan007**

[![GitHub](https://img.shields.io/badge/GitHub-jschouhan007-181717?style=for-the-badge&logo=github)](https://github.com/jschouhan007)

*Made with ❤️ using Java Swing*

</div>

---

## ⭐ Show Your Support

If this project helped you or you found it interesting, please consider giving it a **⭐ Star** on GitHub!

<div align="center">

[![Star this repo](https://img.shields.io/badge/⭐%20Star%20this%20repo-6366f1?style=for-the-badge&logo=github&logoColor=white)](https://github.com/jschouhan007/Telephone-Directory-App-Using-Java-Swing-GUI)

</div>

---

<div align="center">

<!-- FOOTER WAVE -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=6366f1,a855f7&height=100&section=footer" width="100%" />

*Built with Java ☕ · Swing 🪟 · Algorithms 🔬 · Dark UI 🌙*

</div>
