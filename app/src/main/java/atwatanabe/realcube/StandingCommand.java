package atwatanabe.realcube;

public class StandingCommand
{
    private Cube3x3 cube;
    private boolean inverted;

    public StandingCommand(Cube3x3 c, boolean invert)
    {
        cube = c;
        inverted = invert;
    }

    public void execute()
    {
        cube.standing(inverted);
    }

    public void unexecute()
    {
        cube.standing(!inverted);
    }
}
