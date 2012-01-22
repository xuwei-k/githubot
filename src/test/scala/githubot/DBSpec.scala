package githubot

import org.specs2.Specification

class DBSpec extends Specification{ def is=
  "DB" ! e1 ^
  end

  def e1 = {
    val db = new DB[Long](10)
    db.insert(1,1,2,2,2,3,3,4)

    {
      db.selectAll === List(1,2,3,4)
    }and{
      db.insert(2,4,4,5)
      db.selectAll === List(1,2,3,4,5)
    }and{
      db.insert((6L to 12).toSeq :_*)
      db.selectAll === List(3,4,5,6,7,8,9,10,11,12)
    }
  }
}


