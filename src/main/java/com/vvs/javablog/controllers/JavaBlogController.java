package com.vvs.javablog.controllers;

import com.vvs.javablog.models.Post;
import com.vvs.javablog.repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class JavaBlogController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("title", "Developer Posts");
        return "index";
    }
    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("title", "About");
        return "index";
    }
    @GetMapping("/blog")
    public String mainBlog(Model model) {

        Iterable<Post> posts = postRepository.findAll();

        model.addAttribute("posts", posts);
        model.addAttribute("title", "My Posts");

        return "blog-main";
    }
    @GetMapping("/blog/add")
    public String addBlog(Model model) {
        model.addAttribute("title", "Add Post");
        return "blog-add";
    }
    @PostMapping("/blog/add")
    public String postAdd(
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String text,
            Model model) {
        Post post = new Post(title, description, text);
        postRepository.save(post);
        return "redirect:/blog";
    }
    @GetMapping("/blog/{id}")
    public String blogDetails(
            @PathVariable(value = "id") long id,
            Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Post post = postRepository.findById(id).orElseThrow();
        int reviews = post.getReviews() + 1;
        post.setReviews(reviews);
        postRepository.save(post);
        model.addAttribute("post", post);
        model.addAttribute("title", "Blog Details");
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String postEdit(
            @PathVariable(value = "id") long id,
            Model model) {
        if (!postRepository.existsById(id)) {
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> res = new ArrayList();
        post.ifPresent(res::add);
        model.addAttribute("post", res);
        model.addAttribute("title", "Edit Blog");
        return "blog-edit";
    }
    @PostMapping("/blog/{id}/edit")
    public String postEdit(
            @PathVariable(value = "id") long id,
            @RequestParam String title,
            @RequestParam String description,
            @RequestParam String text,
            Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setDescription(description);
        post.setText(text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/delete")
    public String postDelete(
            @PathVariable(value = "id") long id,
           Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }
}