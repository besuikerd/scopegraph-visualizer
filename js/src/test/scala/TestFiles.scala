object TestFiles {
  val test1 =
    """
      |scope graph {
      |  scope #-function_scope-1 {
      |    declarations {
      |      Var{"x" @:9} : TInt()
      |      Var{"y" @:12} : TFloat()
      |    }
      |    references {
      |      Var{"y" @:21}
      |      Var{"x" @:19}
      |    }
      |    direct edges {
      |      P ~ #-module_scope-1
      |    }
      |  }
      |  scope #-s-1 {
      |    declarations {
      |      Module{"C" @:55}
      |      Module{"A" @:7}
      |      Module{"B" @:44}
      |    }
      |  }
      |  scope #-module_scope-2 {
      |    references {
      |      Module{"A" @:45}
      |      Var{"b" @:47}
      |    }
      |    direct edges {
      |      P ~ #-s-1
      |    }
      |    import edges {
      |      I ~ Module{"A" @:45}
      |    }
      |    associated declarations {
      |      I ~ Module{"B" @:44}
      |    }
      |  }
      |  scope #-module_scope-3 {
      |    direct edges {
      |      P ~ #-s-1
      |    }
      |    associated declarations {
      |      I ~ Module{"C" @:55}
      |    }
      |  }
      |  scope #-module_scope-1 {
      |    declarations {
      |      Fun{"f" @:8} : FunctionType(TFloat(),[TInt(),TFloat()])
      |      Var{"b" @:26} : TFloat()
      |    }
      |    references {
      |      Fun{"f" @:27}
      |    }
      |    direct edges {
      |      P ~ #-s-1
      |    }
      |    associated declarations {
      |      I ~ Module{"A" @:7}
      |    }
      |  }
      |  scope #-s_child-1 {
      |    references {
      |      Module{"B" @:56}
      |      Fun{"f" @:58}
      |    }
      |    direct edges {
      |      P ~ #-module_scope-3
      |    }
      |    import edges {
      |      I ~ Module{"B" @:56}
      |    }
      |  }
      |}
    """.stripMargin
}
