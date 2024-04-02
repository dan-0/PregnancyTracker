package me.danlowe.models

data class Checklist(
  // First trimester
  val researchBirthingMethods: Boolean,
  val startPlanningNursery: Boolean,
  val createSavingsPlan: Boolean,
  val researchBirthingFacilities: Boolean,
  val interviewObMidwives: Boolean,
  val discussBirthAnnouncement: Boolean,
  val scheduleFirstSonogram: Boolean,
  val startLookingBabyNames: Boolean,
  // Second trimester
  val addDoctorsToContacts: Boolean,
  val startBuildingNursery: Boolean,
  val discussBabyNames: Boolean,
  val researchDiaperTypes: Boolean,
  val researchCarSeats: Boolean,
  val prepareTakeOffWorkForBirth: Boolean,
  val deepCleanHome: Boolean,
  // Third trimester
  val installCarSeat: Boolean,
  val prepOvernightBag: Boolean,
  val practiceContractionCounter: Boolean,
  val finishNursery: Boolean,
  val clearSpaceForCamera: Boolean,
  val learnRouteToBirthLocation: Boolean,
)
