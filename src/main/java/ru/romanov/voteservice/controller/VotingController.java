package ru.romanov.voteservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.romanov.voteservice.model.Voting;
import ru.romanov.voteservice.model.dto.AnswerForm;
import ru.romanov.voteservice.model.dto.ParticipationForm;
import ru.romanov.voteservice.service.UserService;
import ru.romanov.voteservice.service.VotingService;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class VotingController {

    private final VotingService votingService;
//    private final UserService userService;

    @GetMapping("/voting/create")
    public String showCreateVotingForm(Model model) {
        model.addAttribute("voting", new Voting());
        return "create-voting";
    }

    @PostMapping("/voting/save")
    public String saveVoting(
            @ModelAttribute Voting voting,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при создании голосования");
            return "redirect:/voting/create";
        }
        votingService.save(voting);
        redirectAttributes.addFlashAttribute("success", "Голосование успешно создано!");
        return "redirect:/dashboard";
    }

    @GetMapping("/voting/participate")
    public String showParticipationForm(Model model) {
        model.addAttribute("participationForm", new ParticipationForm());
        model.addAttribute("voting", new Voting());
        return "participate";
    }

    @PostMapping("/voting/participate")
    public String handleParticipation(@Valid @ModelAttribute ParticipationForm form,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) return "participate";

        Voting voting = votingService.findById(form.getVotingId());

        if (voting == null) {
            result.rejectValue("votingId", "error.notfound", "Голосование не найдено");
            return "participate";
        }

        if (voting.getEndDate().isBefore(LocalDateTime.now())) {
            result.rejectValue("votingId", "error.invalid", "Голосование завершено");
            return "participate";
        }

        if (voting.isClosed()) {
            if (!voting.getPassword().equals(form.getPassword())) {
                result.rejectValue("password", "error.invalid", "Неверный пароль");
                model.addAttribute("voting", voting);
                return "participate";
            }
        }

        model.addAttribute("voting", voting);
        model.addAttribute("answerForm", new AnswerForm());
        return "vote";
    }

    @PostMapping("/vote/submit")
    public String submitVote(@ModelAttribute AnswerForm answerForm,
                             @RequestParam UUID votingId) {

        log.info("answerForm {}", answerForm);

        votingService.recordVote(votingId, answerForm.getAnswers());
        return "redirect:/dashboard";
    }
}