package board.annotation;

@Controller
public class HomeController {

    @GetMapping(path = "/")
    public String toMain() {
        return "/";
    }

}
