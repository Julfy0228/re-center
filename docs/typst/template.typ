#import  "titlepage.typ": titlepage

#let template(
  title: str,
  discipline: str,
  theme: str,
  students: array,
  teacher: dictionary,
  numbering: false,
  doc
) = {
  // Страница
  set page(
    paper: "a4",
    margin: (
      left: 30mm,
      right: 15mm,
      top: 20mm,
      bottom: 20mm,
    ),
    footer: context {
      let page_num = counter(page).get().first()
      if page_num == 1 {
        set text(size: 12pt)
        align(center)[Владимир, #datetime.today().year()]
      } else if (numbering) {
        align(center)[#counter(page).display()]
      }
    }
  )

  // Текст
  set text(lang: "ru", font: "Times New Roman", size: 14pt)
  show raw: set text(font: "Courier New", size: 11pt)

  // Заголовки
  show heading: set block(above: 2em, below: 1em)
  
  // Заголовки первого уровня
  show heading.where(level: 1): set text(size: 14pt, weight: "bold")
  show heading.where(level: 1): set align(center)
  show heading.where(level: 1): upper

  // Заголовки второго уровня
  show heading.where(level: 2): set text(size: 14pt, weight: "bold")
  show heading.where(level: 2): set align(center)

  // Заголовки третьего уровня
  show heading.where(level: 3): set text(size: 14pt, weight: "bold")
  show heading.where(level: 3): set align(left)

  // Маркированные списки
  set list(
    indent: 1.5em,
    body-indent: 0.5em,
    spacing: 1em,
  )

  // Нумерованные списки
  set enum(
    numbering: "1)",
    indent: 1.5em,
    body-indent: 0.5em,
    spacing: 1em,
  )
  
  // Титульник
  show: titlepage.with(
    title: title,
    discipline: discipline,
    theme: theme,
    students: students,
    teacher: teacher
  )

  // Абзацы
  set par(
    first-line-indent: (amount: 1.25cm, all: true),
    justify: true,
    leading: 1.5em - 0.75em,
    spacing: 1.5em,
  )

  // Листинги
  show figure.where(kind: raw): set block(breakable: true)

  // Таблицы
  show figure.where(kind: table): set block(breakable: true)

  doc
}