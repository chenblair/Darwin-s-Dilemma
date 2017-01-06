package darwinsdilemma.relogo

import static repast.simphony.relogo.Utility.*;
import static repast.simphony.relogo.UtilityG.*;
import repast.simphony.relogo.Stop;
import repast.simphony.relogo.Utility;
import repast.simphony.relogo.UtilityG;
import repast.simphony.relogo.schedule.Go;
import repast.simphony.relogo.schedule.Setup;
import darwinsdilemma.ReLogoObserver;

class UserObserver extends ReLogoObserver{
	def maxDistance
	def numLivePlayers
	@Setup
	def setup() {
		clearAll()
		ask (patches()) {
			setPcolor(95)
		}
		createPlayers(numPlayers) {
			while (count(other(playersOn(patchHere()))) > 0) {
				setxy(randomPxcor(),randomPycor())
			}
			pickSides()
			initialize()
		}
		maxDistance = (getMaxPxcor() - getMinPxcor())*(getMaxPxcor() - getMinPxcor())
		maxDistance += (getMaxPycor() - getMinPycor())*(getMaxPycor() - getMinPycor())
		maxDistance = Math.sqrt(maxDistance)
	}
	
	@Go
	def go(){
		ask(players()) {
			move()
		}
		ask(players()) {
			reproduce(maxDistance)
		}
		ask(players()) {
			kill()
		}
		numLivePlayers = count(players())
		if (numLivePlayers == 0)
			stop()
	}

}